package wolox.training.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.utils.BuildBook;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.utils.Utils.BODY_WITH_BOOK_CREATED;
import static wolox.training.utils.Utils.BODY_WITH_BOOK_UPDATED;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookRepository mockBookRepository;
    private Book oneTestBook;
    private Book oneTestBookUpdated;
    private Book oneTestBookWithOutGenre;

    @BeforeEach
    public void setUp() {

        oneTestBook = new BuildBook()
            .makeABook(1L, "ficcion", "J.K rowling", "one image", "Harry potter",
                "la camara secreta", " publisher 1234", "1900", 30, "isgn 12");

        oneTestBookUpdated = new BuildBook()
            .makeABook(1L, "ficcion", "un autor", "one image", "el señor de los anillos"
                , "las dos torres", "publisher 1234", "1900", 30, "isbn12");

        oneTestBookWithOutGenre = new BuildBook().makeABook(2L, null, "J.K rowling", "one Image",
            "Harry potter", "la camara secreta", "publishwer 12345", "1900", 30, "isbn12");

    }

    @Test
    void whenFindByIdWhichExists_thenBookIsReturned() throws Exception {
        Mockito.when(mockBookRepository.findById(Mockito.anyLong()))
            .thenReturn(java.util.Optional.ofNullable(oneTestBook));
        String url = ("/api/books/1");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is(oneTestBook.getTitle())))
            .andExpect(jsonPath("$.subtitle", is(oneTestBook.getSubtitle())))
            .andExpect(jsonPath("$.genre", is(oneTestBook.getGenre())));
    }

    @Test
    void whenFindByIdWithOutId_thenThrowException() throws Exception {
        Mockito.when(mockBookRepository.findById(Mockito.anyLong()))
            .thenReturn(java.util.Optional.ofNullable(null));
        String url = ("/api/books/0");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(containsString("Book not found")));
    }


    @Test
    void whenCreateBookWithId_thenBookIsReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        Mockito.when(mockBookRepository.save(Mockito.any()))
            .thenReturn(oneTestBook);

        String url = ("/api/books");
        mvc.perform(
            post(url).contentType(MediaType.APPLICATION_JSON)
                .content(BODY_WITH_BOOK_CREATED))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title", is(oneTestBook.getTitle())))
            .andExpect(jsonPath("$.subtitle", is(oneTestBook.getSubtitle())))
            .andExpect(jsonPath("$.genre", is(oneTestBook.getGenre())));
    }

    @org.junit.Test(expected = NullPointerException.class)
    void whenCreateBookWithoutGenre_throwNullPointerException() throws Exception {
        oneTestBookWithOutGenre.setGenre(null);
        Mockito.when(mockBookRepository.save(Mockito.any()))
            .thenReturn(oneTestBookWithOutGenre);
    }

    @Test
    void whenUpdateBook_thenReturnMismatchException() throws Exception {
        String url = ("/api/books/2");
        mvc.perform(
            put(url).contentType(MediaType.APPLICATION_JSON)
                .content(BODY_WITH_BOOK_UPDATED))
            .andExpect(status().isBadRequest())
            .andExpect(
                content().string(containsString("the id of book is different of parameter")));
    }


    @Test
    void whenUpdateBook_thenBookIsReturnWithaNewsProperties() throws Exception {
        Mockito.when(mockBookRepository.findById(1L))
            .thenReturn(java.util.Optional.ofNullable(oneTestBook));

        Mockito.when(mockBookRepository.save(Mockito.any()))
            .thenReturn(oneTestBookUpdated);

        String url = ("/api/books/1");
        mvc.perform(
            put(url).contentType(MediaType.APPLICATION_JSON)
                .content(BODY_WITH_BOOK_UPDATED))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is(oneTestBookUpdated.getTitle())))
            .andExpect(jsonPath("$.subtitle", is(oneTestBookUpdated.getSubtitle())))
            .andExpect(jsonPath("$.author", is(oneTestBookUpdated.getAuthor())));

    }


}
