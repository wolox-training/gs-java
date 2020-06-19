package wolox.training.repositories;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import wolox.training.models.Users;


@Component
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);

    Optional<ArrayList<Users>> findByBirthdateBetweenAndNameContainingIgnoreCase(
        LocalDate birthdateFrom,
        LocalDate birthdateTo,
        String name);

}
