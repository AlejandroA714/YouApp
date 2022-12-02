package sv.com.udb.youapp.services.authentication.repositories;

import jakarta.validation.metadata.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.udb.youapp.services.authentication.entities.ScopeEntity;

public interface ScopeRepository extends JpaRepository<ScopeEntity, Integer> {
   ScopeEntity findByScope(String scope);
}
