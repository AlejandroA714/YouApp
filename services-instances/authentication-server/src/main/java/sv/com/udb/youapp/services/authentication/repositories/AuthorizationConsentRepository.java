package sv.com.udb.youapp.services.authentication.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.udb.youapp.services.authentication.entities.AuthorizationConsentEntity;

public interface AuthorizationConsentRepository extends
      JpaRepository<AuthorizationConsentEntity, AuthorizationConsentEntity.AuthorizationConsentId> {
   Optional<AuthorizationConsentEntity> findByRegisteredClientIdAndPrincipalName(
         String registeredClientId, String principalName);

   void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId,
         String principalName);
}
