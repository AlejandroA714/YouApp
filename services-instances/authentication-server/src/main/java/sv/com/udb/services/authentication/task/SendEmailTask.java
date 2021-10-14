package sv.com.udb.services.authentication.task;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sv.com.udb.components.mail.sender.model.MailType;
import sv.com.udb.components.mail.sender.services.IEmailService;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;

@Slf4j
@Component
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SendEmailTask implements AuthenticationTask {
   @NonNull
   private IEmailService   emailService;
   private YouAppPrincipal principal;

   @Override
   public void setPrincipal(YouAppPrincipal principal) {
      this.principal = principal;
   }

   @Override
   public void run() {
      try {
         emailService.sendMail(MailType.CONFIRM_MAIL, principal.getEmail(),
               principal.getFields());
      }
      catch (Exception e) {
         LOGGER.error("Failed to send mail");
      }
   }
}
