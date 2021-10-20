package sv.com.udb.services.authentication.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.com.udb.services.authentication.entities.Privilege;
import sv.com.udb.services.authentication.entities.Role;
import sv.com.udb.services.authentication.enums.IPrivilege;

import java.util.List;
import java.util.Optional;

public interface IPrivilegeRepository
      extends JpaRepository<Privilege, Integer> {
   @Query("SELECT p FROM privilege p")
   @EntityGraph(value = "privileges_role",
                type = EntityGraph.EntityGraphType.LOAD)
   List<Privilege> findAllWithRole();
}
