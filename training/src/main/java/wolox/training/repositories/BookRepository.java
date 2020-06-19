package wolox.training.repositories;


import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import wolox.training.models.Book;

@Component
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<ArrayList<Book>> findByPublisherAndGenreAndYear(String publisher, String genre,
        String year);

    Book findFirstByAuthor(String author);

    Optional<Book> findFirstByIsbn(String isbn);

}
