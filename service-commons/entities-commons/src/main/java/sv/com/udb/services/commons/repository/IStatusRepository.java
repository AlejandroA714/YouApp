package sv.com.udb.services.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.udb.services.commons.entities.Status;
import sv.com.udb.services.commons.enums.IStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

public interface IStatusRepository extends JpaRepository<Status, Integer> {
   Status getByStatus(IStatus status);
}
