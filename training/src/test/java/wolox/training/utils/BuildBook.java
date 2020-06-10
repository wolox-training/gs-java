package wolox.training.utils;

import wolox.training.models.Book;

public class BuildBook {

    public Book makeABook(Long id, String genre, String author, String image, String title,
        String subtitle, String publisher, String year, Integer pages, String isbn) {
        Book newBook = new Book(genre, author, image, title, subtitle, publisher, year, pages,
            isbn);
        newBook.setId(id);
        return newBook;
    }
}
