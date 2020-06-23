package wolox.training.repositories;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import wolox.training.models.Users;


@Component
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);

    @Query(value = "SELECT u FROM Users u WHERE UPPER(name) LIKE UPPER(CONCAT(:name,'%')) and (birthdate BETWEEN :birthdateFrom AND :birthdateTo) ")
    Optional<ArrayList<Users>> findByBirthdateBetweenAndNameContainingIgnoreCase(
        @Param("birthdateFrom") LocalDate birthdateFrom,
        @Param("birthdateTo") LocalDate birthdateTo,
        @Param("name") String name,
        Pageable pageable);

}
