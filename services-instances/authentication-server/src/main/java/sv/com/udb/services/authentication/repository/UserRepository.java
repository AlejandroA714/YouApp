package sv.com.udb.services.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.udb.services.authentication.entities.User;

public interface UserRepository extends JpaRepository<User,Integer> {
}
