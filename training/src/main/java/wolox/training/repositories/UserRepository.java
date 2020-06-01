package wolox.training.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import wolox.training.models.Users;

@Component
public interface UserRepository extends JpaRepository<Users, Long> {

    List<Users> findByUsername(String username);

}
