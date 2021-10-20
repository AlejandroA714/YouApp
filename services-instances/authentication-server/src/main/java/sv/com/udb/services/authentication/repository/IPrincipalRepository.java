package sv.com.udb.services.authentication.repository;

import org.hibernate.annotations.NamedQuery;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sv.com.udb.services.authentication.entities.Role;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;

import java.util.List;
import java.util.Optional;

public interface IPrincipalRepository
      extends JpaRepository<YouAppPrincipal, String> {
   @Query("SELECT u FROM user u")
   @EntityGraph(value = "user_roles", type = EntityGraph.EntityGraphType.LOAD)
   List<YouAppPrincipal> findAllWithRoles();

   @Query("SELECT u FROM user u WHERE u.id = ?1")
   @EntityGraph(value = "user_roles", type = EntityGraph.EntityGraphType.LOAD)
   Optional<YouAppPrincipal> findByIdWithRoles(String id);

   @EntityGraph(value = "user_roles", type = EntityGraph.EntityGraphType.LOAD)
   @Query("SELECT u FROM user u WHERE u.email = ?1 OR u.username = ?1 AND u.registrationType.id = 1")
   Optional<YouAppPrincipal> findByUsernameOrEmail(String username);

   @EntityGraph(value = "user_roles", type = EntityGraph.EntityGraphType.LOAD)
   Optional<YouAppPrincipal> findByEmail(String email);
}
