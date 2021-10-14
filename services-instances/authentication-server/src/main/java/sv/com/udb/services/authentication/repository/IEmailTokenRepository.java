package sv.com.udb.services.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.com.udb.services.authentication.entities.EmailToken;

import java.util.List;

public interface IEmailTokenRepository
      extends JpaRepository<EmailToken, Integer> {
   List<EmailToken> getEmailTokenByUserId(String principalId);

   EmailToken getEmailTokenByToken(String token);
}
