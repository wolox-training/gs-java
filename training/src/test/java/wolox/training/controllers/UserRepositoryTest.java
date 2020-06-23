package wolox.training.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.models.Users;
import wolox.training.repositories.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private Users oneTestUser;

    @BeforeEach
    public void setUp() {

        oneTestUser = new Users("pedro", "pedro", LocalDate.parse("1993-12-06"), "hola1234");

    }


    @Test
    public void whenFindByBirthdateBetweenAndNameContainingIgnoreCase_thenReturnOneTestUser() {

        Optional<ArrayList<Users>> found = userRepository
            .findByBirthdateBetweenAndNameContainingIgnoreCase(LocalDate.parse("1990-12-06"),
                LocalDate.parse("2003-12-06"),
                "pe", null);

        assertThat(found.get().get(0).getName())
            .isEqualTo(oneTestUser.getName());
        assertThat(found.get().size())
            .isEqualTo(1);
    }

}
