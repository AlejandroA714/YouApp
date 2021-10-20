package sv.com.udb.services.authentication.task;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sv.com.udb.components.mail.sender.model.MailType;
import sv.com.udb.components.mail.sender.services.IEmailService;
import sv.com.udb.services.authentication.entities.EmailToken;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.repository.IEmailTokenRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SendEmailTask implements AuthenticationTask {
   @NonNull
   private IEmailService         emailService;
   @NonNull
   private IEmailTokenRepository tokenRepository;
   private YouAppPrincipal       principal;

   @Override
   public void setPrincipal(YouAppPrincipal principal) {
      this.principal = principal;
   }

   @Override
   public void run() {
      try {
         EmailToken token = EmailToken.builder()
               .token(UUID.randomUUID().toString())
               .expiration(
                     LocalDateTime.now(ZoneId.of("GMT-06:00")).plusHours(1))
               .user(principal).build();
         tokenRepository.save(token);
         Map<String, Object> props = new HashMap<>();
         props.put("TOKEN", token.getToken());
         emailService.sendMail(MailType.CONFIRM_MAIL, principal.getEmail(),
               props);
      }
      catch (Exception e) {
         LOGGER.error("Failed to send mail");
      }
   }
}
