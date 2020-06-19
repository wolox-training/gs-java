package wolox.training.controllers;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wolox.training.models.Book;
import wolox.training.models.Users;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.utils.BuildBook;
import wolox.training.utils.BuildUser;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.utils.Utils.BODY_WITH_USER;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsersControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    private UserRepository mockUserRepository;
    private Users oneTestUser;
    private Book oneTestBook;
    private Book oneTestBookWithoutUser;

    @MockBean
    private BookRepository mockBookRepository;


    @BeforeEach
    public void setUp() {

        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        oneTestBook = new BuildBook()
            .makeABook(1L, "ficcion", "J.K rowling", "one image", "Harry potter",
                "la camara secreta", " publisher 1234", "1900", 30, "isgn 12");

        oneTestBookWithoutUser = new BuildBook().makeABook(2L, null, "J.K rowling", "one Image",
            "Harry potter 2", "la orden del fenix", "publishwer 22222", "1900", 30, "isbn12");

        // user with Id
        oneTestUser = new BuildUser()
            .makeAUser(1L, "pedro1234", "pedro", LocalDate.parse("1993-12-06"), "hola1234",
                oneTestBook);
    }

    @WithMockUser(value = "pedro")
    @Test
    void whenFindByIdWhichExists_thenUserIsReturned() throws Exception {
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(java.util.Optional.ofNullable(oneTestUser));
        String url = ("/api/books/users/1");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is(oneTestUser.getUsername())))
            .andExpect(jsonPath("$.name", is(oneTestUser.getName())))
            .andExpect(jsonPath("$.birthdate", is(oneTestUser.getBirthdate().toString())));
    }

    @Test
    void whenFindByIdWhichExistsWithOutAutenticate_thenReturnAccessDenied() throws Exception {
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(java.util.Optional.ofNullable(oneTestUser));
        String url = ("/api/books/users/1");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", is("Access denied")));
    }

    @WithMockUser(value = "pedro")
    @Test
    void whenFindByIdWithOutId_thenThrowException() throws Exception {
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(java.util.Optional.ofNullable(oneTestUser));
        String url = ("/api/books/users/0");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(containsString("User not found")));
    }

    @WithMockUser(value = "pedro")
    @Test
    void whenDeleteBookOfUser_thenThrowBookNotOwnedException() throws Exception {
        Mockito.when(mockUserRepository.findById(Mockito.any()))
            .thenReturn(java.util.Optional.ofNullable(oneTestUser));
        Mockito.when(mockBookRepository.findById(Mockito.any()))
            .thenReturn(java.util.Optional.ofNullable(oneTestBookWithoutUser));

        String url = ("/api/books/users/1/book/2");
        mvc.perform(
            delete(url).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(containsString("the book not belongs to the owner")));
    }

    @WithMockUser(value = "pedro")
    @Test
    void whenCreateUserWithId_thenUserIsReturn() throws Exception {
        Mockito.when(mockUserRepository.save(Mockito.any()))
            .thenReturn(oneTestUser);

        String url = ("/api/books/users");
        mvc.perform(
            post(url).contentType(MediaType.APPLICATION_JSON)
                .content(BODY_WITH_USER))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username", is(oneTestUser.getUsername())))
            .andExpect(jsonPath("$.name", is(oneTestUser.getName())))
            .andExpect(jsonPath("$.birthdate", is(oneTestUser.getBirthdate().toString())));
    }

    @WithMockUser(value = "pedro")
    @Test
    void whenUpdateBookOfUser_thenUserIsReturnWithaNewBook() throws Exception {
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(java.util.Optional.ofNullable(oneTestUser));
        Mockito.when(mockBookRepository.findById(2L))
            .thenReturn(java.util.Optional.ofNullable(oneTestBookWithoutUser));

        Mockito.when(mockUserRepository.save(Mockito.any()))
            .thenReturn(oneTestUser);

        String url = ("/api/books/users/1/book/2");
        mvc.perform(
            put(url).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username", is(oneTestUser.getUsername())))
            .andExpect(jsonPath("$.name", is(oneTestUser.getName())))
            .andExpect(jsonPath("$.birthdate", is(oneTestUser.getBirthdate().toString())))
            .andExpect(jsonPath("$.books", hasSize(2)));

    }

    @WithMockUser(value = "pedro")
    @Test
    void whenUpdateBookOfUserByBookAlradyOwned_thenThrowBookAlradyOwnedException()
        throws Exception {
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(java.util.Optional.ofNullable(oneTestUser));
        Mockito.when(mockBookRepository.findById(1L))
            .thenReturn(java.util.Optional.ofNullable(oneTestBook));

        String url = ("/api/books/users/1/book/1");
        mvc.perform(
            put(url).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(containsString("the book already belongs to the owner")));

    }


}
