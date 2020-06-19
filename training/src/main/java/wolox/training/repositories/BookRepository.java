package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import wolox.training.models.Book;

@Component
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findFirstByAuthor(String author);

    Optional<Book> findFirstByIsbn(String isbn);

}
