package sv.com.udb.youapp.services.authentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.udb.youapp.services.authentication.entities.RegisteredClientEntity;

import java.util.Optional;

public interface JpaClientRepository
      extends JpaRepository<RegisteredClientEntity, String> {
   Optional<RegisteredClientEntity> findByClientId(String clientId);
}
