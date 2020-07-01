package wolox.training.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.util.ArrayList;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.utils.BuildBook;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.utils.Utils.BODY_WITH_BOOK_CREATED;
import static wolox.training.utils.Utils.BODY_WITH_BOOK_UPDATED;
import static wolox.training.utils.Utils.RESPONSE_ISBN_BOOK;
import static wolox.training.utils.Utils.RESQUEST_BAD_ISBN_BOOK;
import static wolox.training.utils.Utils.RESQUEST_ISBN_BOOK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();
    ArrayList<Book> oneArrayTest;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @MockBean
    private BookRepository mockBookRepository;
    private Book oneTestBook;
    private Book oneTestBookUpdated;
    private Book oneTestBookWithOutGenre;
    private Book oneTestBookWithIsbn;

    @BeforeEach
    public void setUp() {

        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        oneTestBook = new BuildBook()
            .makeABook(1L, "ficcion", "J.K rowling", "one image", "Harry potter",
                "la camara secreta", " publisher 1234", "1900", 30, "isgn 12");

        oneTestBookUpdated = new BuildBook()
            .makeABook(1L, "ficcion", "un autor", "one image", "el señor de los anillos"
                , "las dos torres", "publisher 1234", "1900", 30, "isbn12");

        oneTestBookWithOutGenre = new BuildBook().makeABook(2L, null, "J.K rowling", "one Image",
            "Harry potter", "la camara secreta", "publishwer 12345", "1900", 30, "isbn12");

        oneTestBookWithIsbn = new BuildBook()
            .makeABook(3L, "Caricatures and cartoons-Zen Buddhism", "Zhizhong Cai",
                "https://covers.openlibrary.org/b/id/240726-S.jpg",
                "Zen speaks",
                "shouts of nothingness", "Anchor Books", "1994", 159, "0385472579");

        oneArrayTest = new ArrayList<Book>();
        oneArrayTest.add(oneTestBook);
        oneArrayTest.add(oneTestBookWithIsbn);
    }


    @WithMockUser(value = "pedro")
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

    @WithMockUser(value = "pedro")
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


    @WithMockUser(value = "pedro")
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

    @WithMockUser(value = "pedro")
    @org.junit.Test(expected = NullPointerException.class)
    void whenCreateBookWithoutGenre_throwNullPointerException() throws Exception {
        oneTestBookWithOutGenre.setGenre(null);
        Mockito.when(mockBookRepository.save(Mockito.any()))
            .thenReturn(oneTestBookWithOutGenre);
    }

    @WithMockUser(value = "pedro")
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
    void whenUpdateBookWithOutAutenticate_thenReturnAccessDenied() throws Exception {
        String url = ("/api/books/2");
        mvc.perform(
            put(url).contentType(MediaType.APPLICATION_JSON)
                .content(BODY_WITH_BOOK_UPDATED))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", is("Access denied")));
    }

    @WithMockUser(value = "pedro")
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


    @WithMockUser(value = "pedro")
    @Test
    void whenFindByPublisherAndGenreAndYearWithoutParams_thenReturnABook() throws Exception {
        Mockito.when(mockBookRepository
            .findByPublisherAndGenreAndYear(
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any()))
            .thenReturn(java.util.Optional.ofNullable(oneArrayTest));
        String url = ("/api/books/findBook");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].title", is(oneTestBook.getTitle())));
    }


    @WithMockUser(value = "pedro")
    @Test
    void whenFindByPublisherAndGenreAndYearAndPassParams_thenReturnABook() throws Exception {
        Mockito.when(mockBookRepository
            .findByPublisherAndGenreAndYear(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any()))
            .thenReturn(java.util.Optional.ofNullable(oneArrayTest));

        String url = ("/api/books/findBook");

        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON)
            .param("publisher", oneTestBook.getPublisher())
            .param("genre", oneTestBook.getGenre())
            .param("year", oneTestBook.getYear())
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].title", is(oneTestBook.getTitle())));
    }

    @WithMockUser(value = "pedro")
    @Test
    void whengetAllByConditionsWithPageable_thenReturnsBooks() throws Exception {

        Pageable pageable = PageRequest.of(0, 2, Sort.by(
            Order.asc("id")));
        Mockito.when(mockBookRepository.getAllByConditions(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            pageable
        )).thenReturn(java.util.Optional.ofNullable(oneArrayTest));

        String url = ("/api/books");

        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON)
            .param("page", "0")
            .param("size", "2")
            .param("sort", "id,asc")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].title", is(oneTestBook.getTitle())))
            .andExpect(jsonPath("$[1].id", is(3)))
            .andExpect(jsonPath("$[1].title", is(oneTestBookWithIsbn.getTitle())));

    }

    @WithMockUser(value = "pedro")
    @Test
    void whenFindFirstByIsbnInService_thenThrowsException() throws Exception {
        Mockito.when(mockBookRepository.findFirstByIsbn("0385472"))
            .thenReturn(java.util.Optional.ofNullable(null));

        stubFor(WireMock.get(urlEqualTo(
            RESQUEST_BAD_ISBN_BOOK))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody("{}")));

        String url = ("/api/books/isbn/0385472");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().string(containsString("Book not found")));
    }

    @WithMockUser(value = "pedro")
    @Test
    void whenFindFirstByIsbnInService_thenReturnABook() throws Exception {
        Mockito.when(mockBookRepository.findFirstByIsbn("0385472579"))
            .thenReturn(java.util.Optional.ofNullable(null));

        Mockito.when(mockBookRepository.save(Mockito.any()))
            .thenReturn(oneTestBookWithIsbn);

        stubFor(WireMock.get(urlEqualTo(
            RESQUEST_ISBN_BOOK))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody(RESPONSE_ISBN_BOOK)));

        String url = ("/api/books/isbn/0385472579");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isbn", is(oneTestBookWithIsbn.getIsbn())));

    }


}
