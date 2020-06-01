package wolox.training.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.Users;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("api/books")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;


    @GetMapping("/users")
    public Iterable findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public Users findOne(@PathVariable Long id) {
        return userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }


    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }


    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public Users create(@RequestBody Users user) {
        return userRepository.save(user);
    }

    @PutMapping("/users/{id}")
    public Users updateUser(@RequestBody Users users, @PathVariable Long id) {
        if (users.getId() != id) {
            throw new UserNotFoundException();
        }
        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
        return userRepository.save(users);
    }

    @PutMapping("/users/{id_user}/book/{id_book}")
    public Users updateBookOfUser(@PathVariable String id_user, @PathVariable String id_book) {
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
