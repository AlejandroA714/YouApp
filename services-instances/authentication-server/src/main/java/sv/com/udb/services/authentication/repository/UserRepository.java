package sv.com.udb.services.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.udb.services.authentication.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
