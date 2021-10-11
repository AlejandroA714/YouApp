package sv.com.udb.services.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.udb.services.authentication.entities.OAuthRegistrationType;
import sv.com.udb.services.authentication.enums.IOAuthRegistrationType;

public interface IOAuthRegistrationRepository
      extends JpaRepository<OAuthRegistrationType, Integer> {
   OAuthRegistrationType findOAuthRegistrationTypeByName(
         IOAuthRegistrationType registrationType);
}
