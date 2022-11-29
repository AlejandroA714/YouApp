package sv.com.udb.youapp.commons.jpa.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sv.com.udb.youapp.commons.jpa.entities.RoleEntity;

import java.util.List;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
   @Query("SELECT r FROM role r")
   @EntityGraph(value = "rolesWithUsers",
                type = EntityGraph.EntityGraphType.LOAD)
   List<RoleEntity> findAllWithRoles();
}
