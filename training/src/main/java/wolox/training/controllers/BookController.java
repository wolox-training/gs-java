package wolox.training.controllers;

import io.swagger.annotations.Api;
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
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.OpenLibraryService;

@RestController
@RequestMapping("api/books")
@Api(value = "Book Resouce Rest endpoint")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    @GetMapping
    public Iterable findAll() {
        return bookRepository.findAll();
    }

    @GetMapping("/isbn/{isbn}")
    public Book bookInfoByIsbn(@PathVariable String isbn) throws BookNotFoundException {
        return bookRepository.findFirstByIsbn(isbn)
            .orElseGet(() -> {
                Book bookService = openLibraryService.bookInfo(("ISBN:").concat(isbn))
                    .orElseThrow(BookNotFoundException::new);
                return bookRepository.save(bookService);
            });

    }

    @GetMapping("/title/{author}")
    public Book findFirstByAuthor(@PathVariable String author) {
        return bookRepository.findFirstByAuthor(author);
    }

    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) {
        return bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id)
        throws BookIdMismatchException {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        Book bookToUpdate = bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);
        bookToUpdate.update(book);
        return bookRepository.save(bookToUpdate);
    }

}

