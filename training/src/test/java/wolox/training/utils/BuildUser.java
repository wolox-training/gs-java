package wolox.training.utils;


import java.time.LocalDate;
import wolox.training.models.Book;
import wolox.training.models.Users;

public class BuildUser {

    public Users makeAUser(Long id, String username, String name, LocalDate birthdate,
        Book oneBook) {
        Users newUser = new Users(username, name, birthdate);
        newUser.setId(id);
        newUser.addBook(oneBook);
        return newUser;
    }
}
