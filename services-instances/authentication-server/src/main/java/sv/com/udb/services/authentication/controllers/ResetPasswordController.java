package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sv.com.udb.components.mail.sender.model.MailType;
import sv.com.udb.components.mail.sender.services.IEmailService;
import sv.com.udb.services.authentication.exceptions.InvalidTokenException;
import sv.com.udb.services.authentication.models.ChangePasswordRequest;
import sv.com.udb.services.authentication.services.IAuthenticationService;
import sv.com.udb.services.commons.entities.YouAppPrincipal;
import sv.com.udb.services.commons.exceptions.PrincipalDoesNotExist;
import sv.com.udb.services.commons.repository.IPrincipalRepository;
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
   @NonNull
   private final IAuthenticationService     authenticationService;
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

   @PostMapping("/")
   public void change(@RequestBody ChangePasswordRequest request,
         @RequestHeader(name = "Authorization") String token) {
      if (null == token || !token.contains("Bearer "))
         throw new InvalidTokenException("Needs a bearer token");
      try {
         authenticationService.changePassword(token.substring(7), request);
      }
      catch (Exception e) {
         throw e;
      }
   }
}
