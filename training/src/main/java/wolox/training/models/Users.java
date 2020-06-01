package wolox.training.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.base.Preconditions;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
@ApiModel(description = "Users from the OpenLibraryApi")
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @ApiModelProperty(notes = "username of user")
    private String username;

    @NotNull
    @ApiModelProperty(notes = "name of user")
    private String name;

    @NotNull
    @ApiModelProperty(notes = "birthdate of user")
    private LocalDate birthdate;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<Book> books = new ArrayList();

    public Users() {
    }

    public Users(String username, String name, LocalDate birthdate) {
        this.username = username;
        this.name = name;
        this.birthdate = birthdate;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

        String message = "the username of user must not be null";
        Preconditions.checkNotNull(username, message);
        username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String message = "the name of user must not be null";
        Preconditions.checkNotNull(name, message);
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        String messageCheckNotNull = "the birthdate of user must not be null";
        String messagCheckArgument = "the birthdate is greater than the current";
        Preconditions.checkNotNull(birthdate, messageCheckNotNull);
        Preconditions.checkArgument(birthdate.isBefore(LocalDate.now()), messagCheckArgument);
        this.birthdate = birthdate;
    }

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public boolean checkUserHasABook(Book bookToCheck) {
        return this.books.stream()
            .anyMatch(book -> book.getId() == bookToCheck.getId());
    }
}
