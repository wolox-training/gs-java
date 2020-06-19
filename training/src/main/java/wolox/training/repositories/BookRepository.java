package wolox.training.repositories;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import wolox.training.models.Book;

@Component
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT b FROM Book b WHERE (:genre IS NULL OR genre = :genre) and  (:year IS NULL OR year = :year) and (:publisher IS NULL OR publisher = :publisher)")
    Optional<ArrayList<Book>> findByPublisherAndGenreAndYear(
        @Param("publisher") String publisher,
        @Param("genre") String genre,
        @Param("year") String year);

    Book findFirstByAuthor(String author);

    Optional<Book> findFirstByIsbn(String isbn);

}
