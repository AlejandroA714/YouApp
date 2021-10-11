package sv.com.udb.services.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.udb.services.authentication.entities.Role;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;

import java.util.Optional;

public interface IPrincipalRepository
      extends JpaRepository<YouAppPrincipal, String> {
   Optional<YouAppPrincipal> findByUsernameOrEmail(String username,
         String email);
}
