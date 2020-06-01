package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
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

    @ApiModelProperty(notes = "genre of book")
    private String genre;

    @Column(nullable = false)
    @ApiModelProperty(notes = "author of book")
    private String author;

    @Column(nullable = false)
    @ApiModelProperty(notes = "image of book")
    private String image;

    @Column(nullable = false)
    @ApiModelProperty(notes = "title of book")
    private String title;

    @Column(nullable = false)
    @ApiModelProperty(notes = "subtitle of book")
    private String subtitle;

    @Column(nullable = false)
    @ApiModelProperty(notes = "publisher of book")
    private String publisher;

    @Column(nullable = false)
    @ApiModelProperty(notes = "year of book")
    private String year;

    @Column(nullable = false)
    @ApiModelProperty(notes = "pages of book")
    private Integer pages;

    @Column(nullable = false)
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

    public void setId(long id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
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
    }
}
