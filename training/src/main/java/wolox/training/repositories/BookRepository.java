package wolox.training.repositories;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
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
        @Param("year") String year,
        Pageable pageable);

    Book findFirstByAuthor(String author);

    @Query(value = "SELECT b FROM Book b WHERE "
        + "(:genre IS NULL OR genre = :genre) and  "
        + "(:author IS NULL OR author = :author) and "
        + "(:image IS NULL OR image = :image) and"
        + "(:title IS NULL OR title = :title) and"
        + "(:subtitle IS NULL OR subtitle = :subtitle) and"
        + "(:publisher IS NULL OR publisher = :publisher) and"
        + "(:year IS NULL OR year = :year) and"
        + "(:pages IS NULL OR pages = :pages) and"
        + "(:isbn IS NULL OR isbn = :isbn)")
    Optional<ArrayList<Book>> getAllByConditions(
        @Param("genre") String genre,
        @Param("author") String author,
        @Param("image") String image,
        @Param("title") String title,
        @Param("subtitle") String subtitle,
        @Param("publisher") String publisher,
        @Param("year") String year,
        @Param("pages") Integer pages,
        @Param("isbn") String isbn,
        Pageable pageable
    );

    Optional<Book> findFirstByIsbn(String isbn);


}
