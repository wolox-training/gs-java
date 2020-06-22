package wolox.training.controllers;

import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private Book oneTestBook;

    @BeforeEach
    public void setUp() {

        oneTestBook = new Book("Caricatures and cartoons-Zen Buddhism", "Zhizhong Cai",
            "https://covers.openlibrary.org/b/id/240726-S.jpg", "Zen speaks",
            "shouts of nothingness", "Anchor Books", "1994", 159, "0385472579");

    }


    @Test
    public void whenFindByPublisherAndGenreAndYear_thenReturnOneTestBook() {

        Optional<ArrayList<Book>> found = bookRepository
            .findByPublisherAndGenreAndYear(oneTestBook.getPublisher(), oneTestBook.getGenre(),
                oneTestBook.getYear());

        assertThat(found.get().get(0).getTitle())
            .isEqualTo(oneTestBook.getTitle());
        assertThat(found.get().size())
            .isEqualTo(1);
    }

    @Test
    public void whenGetAllByConditions_thenReturnOneTestBook() {

        Optional<ArrayList<Book>> found = bookRepository
            .getAllByConditions(
                oneTestBook.getGenre(),
                oneTestBook.getAuthor(),
                oneTestBook.getImage(),
                oneTestBook.getTitle(),
                oneTestBook.getSubtitle(),
                oneTestBook.getPublisher(),
                oneTestBook.getYear(),
                oneTestBook.getPages(),
                oneTestBook.getIsbn()
            );

        assertThat(found.get().get(0).getTitle())
            .isEqualTo(oneTestBook.getTitle());
        assertThat(found.get().size())
            .isEqualTo(1);
    }

}
