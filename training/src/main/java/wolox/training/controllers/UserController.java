package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.BookNotOwnedException;
import wolox.training.exceptions.NotMatchConfirmationPasswordException;
import wolox.training.exceptions.NotMatchPasswordException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.PasswordUpdate;
import wolox.training.models.Users;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("api/books")
@Api(value = "User Resouce Rest endpoint")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/users")
    @ApiOperation(value = "find all users", response = Users.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Succesfully find all user"),
    })
    public Iterable findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    @ApiOperation(value = "Giving an id, return a book", response = Users.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Succesfully retrieved book"),
        @ApiResponse(code = 404, message = "User not found")
    })
    public Users findOne(@ApiParam(value = "id to find the user")
    @PathVariable Long id
    ) {
        return userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }

    @DeleteMapping("/users/{id}")
    @ApiOperation(value = "Giving an id, delete a user")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Succesfully delete user"),
        @ApiResponse(code = 404, message = "User not found")
    })
    public void delete(@PathVariable Long id) {
        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);

    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a user", response = Users.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Succesfully create user")
    })
    public Users create(
        @ApiParam(value = "user DTO in body to create it") @RequestBody Users user) {

        Users newUser = new Users();

        newUser.setName(user.getName());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setBirthdate(user.getBirthdate());

        return userRepository.save(newUser);

    }

    @PutMapping("/users/resetpassword/{id}")
    @ApiOperation(value = "Giving an id, update password of user", response = Users.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Succesfully update password user"),
        @ApiResponse(code = 404, message = "User not found")
    })
    public Users updatePasswordUser(
        @ApiParam(value = "user id to found it") @PathVariable Long id,
        @ApiParam(value = "password to update for the user") @RequestBody PasswordUpdate passwordToUpdate
    ) {
        Users user = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(passwordToUpdate.getOldPassword(), user.getPassword())) {
            if (passwordToUpdate.getConfirmNewPassword()
                .equals(passwordToUpdate.getNewPassword())) {
                user.setPassword(passwordEncoder.encode(passwordToUpdate.getNewPassword()));
                return userRepository.save(user);
            } else {
                throw new NotMatchConfirmationPasswordException();
            }
        } else {
            throw new NotMatchPasswordException();
        }
    }


    @PutMapping("/users/{id}")
    @ApiOperation(value = "Giving an id, update a user", response = Users.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Succesfully update user"),
        @ApiResponse(code = 404, message = "User not found")
    })
    public Users updateUser(
        @ApiParam(value = "user DTO in body to update it") @RequestBody Users users,
        @ApiParam(value = "user id to found it") @PathVariable Long id) {
        if (users.getId() != id) {
            throw new UserNotFoundException();
        }
        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        return userRepository.save(users);
    }

    @DeleteMapping("/users/{id_user}/book/{id_book}")
    @ApiOperation(value = "Giving an id of user and id ok book and delete relationship")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "delete create relationship"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 404, message = "Book not found")
    })
    public Users deleteBookOfUser(
        @ApiParam(value = "id of user to delete relationship") @PathVariable String id_user,
        @ApiParam(value = "id of book to delete relationship")
        @PathVariable String id_book) {
        Users user = userRepository.findById(Long.parseLong(id_user))
            .orElseThrow(UserNotFoundException::new);
        Book bookToAssing = bookRepository.findById(Long.parseLong(id_book))
            .orElseThrow(BookNotFoundException::new);
        if (user.checkUserHasABook(bookToAssing)) {
            user.removeBook(bookToAssing);
            return userRepository.save(user);
        } else {
            throw new BookNotOwnedException();
        }
    }

    @GetMapping("/findUser")
    @ApiOperation(value = "Get the currently logged in user", response = Users.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Succesfully found users by birthdate and name"),
        @ApiResponse(code = 404, message = "User not found")
    })
    public ArrayList<Users> findByBirthdateAndName(@RequestParam String birthdateFrom,
        @RequestParam String birthdateTo,
        @RequestParam String name) {
        return userRepository
            .findByBirthdateBetweenAndNameContainingIgnoreCase(LocalDate.parse(birthdateFrom),
                LocalDate.parse(birthdateTo), name).orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("/username")
    @ResponseBody
    @ApiOperation(value = "Get the currently logged in user", response = Users.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Succesfully found username")
    })
    public String currentUserName(Principal principal) {
        return principal.getName();
    }

    @PutMapping("/users/{id_user}/book/{id_book}")
    @ApiOperation(value = "Giving an id of user and id ok book and create relationship", response = Users.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Succesfully create relationship"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 404, message = "Book not found")
    })
    public Users updateBookOfUser(
        @ApiParam(value = "id of user to create relationship") @PathVariable String id_user,
        @ApiParam(value = "id of book to create relationship")
        @PathVariable String id_book) {
        Users user = userRepository.findById(Long.parseLong(id_user))
            .orElseThrow(UserNotFoundException::new);
        Book bookToAssing = bookRepository.findById(Long.parseLong(id_book))
            .orElseThrow(BookNotFoundException::new);
        if (user.checkUserHasABook(bookToAssing)) {
            throw new BookAlreadyOwnedException();
        } else {
            user.addBook(bookToAssing);
            return userRepository.save(user);
        }
    }
}
