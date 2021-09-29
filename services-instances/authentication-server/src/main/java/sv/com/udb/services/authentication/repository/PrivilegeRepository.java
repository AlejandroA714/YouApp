package sv.com.udb.services.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.udb.services.authentication.entities.Privilege;
import sv.com.udb.services.authentication.entities.Role;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
}
