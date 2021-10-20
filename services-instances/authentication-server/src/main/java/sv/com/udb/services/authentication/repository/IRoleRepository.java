package sv.com.udb.services.authentication.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sv.com.udb.services.authentication.entities.Role;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.enums.IRole;

import java.util.List;
import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Integer> {
   @Query("SELECT r FROM role r")
   @EntityGraph(value = "roles_privileges",
                type = EntityGraph.EntityGraphType.LOAD)
   List<Role> findAllWithPrivileges();

   @Query("SELECT r FROM role r")
   @EntityGraph(value = "roles_principals",
                type = EntityGraph.EntityGraphType.LOAD)
   List<Role> findAllWithPrincipal();
}
