package sv.com.udb.services.commons.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.com.udb.services.commons.entities.Privilege;

import java.util.List;

public interface IPrivilegeRepository
      extends JpaRepository<Privilege, Integer> {
   @Query("SELECT p FROM privilege p")
   @EntityGraph(value = "privileges_role",
                type = EntityGraph.EntityGraphType.LOAD)
   List<Privilege> findAllWithRole();
}
