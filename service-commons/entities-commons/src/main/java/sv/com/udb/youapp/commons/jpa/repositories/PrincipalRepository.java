package sv.com.udb.youapp.commons.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sv.com.udb.youapp.commons.jpa.entities.PrincipalEntity;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface PrincipalRepository extends JpaRepository<PrincipalEntity, String> {

 @Query("SELECT p from principal p WHERE p.username = ?1 OR p.email = ?1")
 Optional<PrincipalEntity> findByUsername(String username);

}
