package sv.com.udb.services.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sv.com.udb.services.authentication.entities.Role;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
   // @Query("SELECT u FROM User u JOIN user_roles ON u.Id = user_roles.user_id
   // JOIN role ON role.Id = :roleId")
   // List<YouAppPrincipal> getUserByRole(@Param("roleId") String roleId);
}
