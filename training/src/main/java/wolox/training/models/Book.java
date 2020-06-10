package wolox.training.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.Preconditions;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
@ApiModel(description = "Books from the OpenLibraryApi")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @ApiModelProperty(notes = "genre of book")
    private String genre;
    @NotNull
    @ApiModelProperty(notes = "author of book")
    private String author;
    @NotNull
    @ApiModelProperty(notes = "image of book")
    private String image;
    @NotNull
    @ApiModelProperty(notes = "title of book")
    private String title;
    @NotNull
    @ApiModelProperty(notes = "subtitle of book")
    private String subtitle;
    @ApiModelProperty(notes = "publisher of book")
    private String publisher;
    @ApiModelProperty(notes = "year of book")
    private String year;
    @NotNull
    @ApiModelProperty(notes = "pages of book")
    private Integer pages;
    @ApiModelProperty(notes = "isbn of book")
    private String isbn;
    @ManyToMany(mappedBy = "books")
    @ApiModelProperty(notes = "users who have the book")
    private List<Users> users = new ArrayList();


    public Book() {
    }

    public Book(String genre, String author, String image, String title, String subtitle,
        String publisher,
        String year, Integer pages, String isbn) {
        this.genre = genre;
        this.author = author;
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
        this.publisher = publisher;
        this.year = year;
        this.pages = pages;
        this.isbn = isbn;

    }

    public long getId() {
        return id;
    }

    // this method is used in tests
    public void setId(long id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        String message = "the genre of book must not be null";
        Preconditions.checkNotNull(genre, message);
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        String message = "the author of book must not be null";
        Preconditions.checkNotNull(author, message);
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        String message = "the image of book must not be null";
        Preconditions.checkNotNull(image, message);
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String message = "the title of book must not be null";
        Preconditions.checkNotNull(title, message);
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        String message = "the subtitle of book must not be null";
        Preconditions.checkNotNull(subtitle, message);
        this.subtitle = subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        String messagCheckNotNull = "the pages of book must not be null";
        String messagCheckArgument = "the pages of book must be greater than 0";
        Preconditions.checkNotNull(pages, messagCheckNotNull);
        Preconditions.checkArgument(pages > 0, messagCheckArgument);
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List<Users> getUsers() {
        return (List<Users>) Collections.unmodifiableList(users);
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public void addUser(Users user) {
        this.users.add(user);
    }

    public void deleteAllUser() {
        this.users.removeAll(this.users);
    }

    public void update(Book book) {
        this.setAuthor(book.getAuthor());
        this.setGenre(book.getGenre());
        this.setImage(book.getImage());
        this.setIsbn(book.getIsbn());
        this.setPages(book.getPages());
        this.setPublisher(book.getPublisher());
        this.setSubtitle(book.getSubtitle());
        this.setTitle(book.getTitle());
        this.setYear(book.getYear());

    }
}
