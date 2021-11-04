package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sv.com.udb.components.mail.sender.model.MailType;
import sv.com.udb.components.mail.sender.services.IEmailService;
import sv.com.udb.services.commons.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.exceptions.PrincipalDoesNotExist;
import sv.com.udb.services.authentication.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.services.IEncryptionPasswordService;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth/reset-password")
public class ResetPasswordController {
   @NonNull
   private final IEncryptionPasswordService passwordService;
   @NonNull
   private final IEmailService              emailService;
   @NonNull
   private final IPrincipalRepository       principalRepository;
   private static final String              NEW_PASS = "newpass";

   @GetMapping("/{email}")
   public void reset(@PathVariable String email) {
      try {
         Optional<YouAppPrincipal> _principal = principalRepository
               .findByEmail(email);
         if (!_principal.isPresent()) throw new PrincipalDoesNotExist(email);
         YouAppPrincipal principal = _principal.get();
         String passwd = passwordService.generateRandomPassword(12);
         Map<String, Object> props = principal.getSummary();
         props.put(NEW_PASS, passwd);
         emailService.sendMail(MailType.RECOVER_PASWORD, principal.getEmail(),
               props);
         principal.setPassword(passwordService.encryptPassword(passwd));
         principalRepository.save(principal);
      }
      catch (Exception e) {
         LOGGER.error("{}", e);
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
               "Failed to recover passwrod, try againg");
      }
   }

   @GetMapping("/decrypt")
   public String ff() {
      try {
         return passwordService.decryptPassword(
               "F3Y77xuxcSewvtqHH1XCg4ARZxx8HGcVW/+tzMMJ/JURS9rj8qFQUZvdHvqkes/wk/LesaQNYxo1cul9zqVN25E000V2wm1m5qgI1pC4vLUGxN7RNIh7IxIXhnVH8FIvVebHzv48D+pg7B8BrDzpySeDXEBc2G2B7BhXiDgPodBm+dO6F1xe2zE1NgNMPIEkyhlNledPRAVKKyn4naZmPy6+Y79M2lh1X8+XMD8JvEET1/aHnZ/L50F6vXiPmLaCwft0HOV313TmEj9m6pkHx1G3ON1khNmdbwLWggjskicavV14tRpZnHgB0jPIHYP2dzKydRr4Mf+6hj901hsC5g==");
      }
      catch (Exception e) {
         e.printStackTrace();
         return "";
      }
   }
}
