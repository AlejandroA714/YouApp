package sv.com.udb.services.authentication.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.com.udb.services.commons.entities.Role;

import java.util.List;

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
