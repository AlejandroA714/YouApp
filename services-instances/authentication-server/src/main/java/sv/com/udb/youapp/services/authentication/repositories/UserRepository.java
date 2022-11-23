package sv.com.udb.youapp.services.authentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.udb.youapp.services.authentication.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
