package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sv.com.udb.components.minio.client.services.IMinioService;
import sv.com.udb.services.authentication.entities.AbstractPrincipal;
import sv.com.udb.services.authentication.entities.GoogleAuthorizationRequest;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.exceptions.InvalidTokenException;
import sv.com.udb.services.authentication.exceptions.RegistrationException;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.authentication.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.services.IAuthenticationService;
import sv.com.udb.services.authentication.services.IEncryptionPasswordService;
import sv.com.udb.services.authentication.services.IGoogleOAuth2Provider;
import sv.com.udb.services.authentication.task.AuthenticationTask;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthenticationController {
   @NonNull
   private final IAuthenticationService authService;

   @GetMapping("/confirm_email")
   public void validateToken(@RequestParam String token) {
      try {
         authService.validateToken(token);
      }
      catch (Exception e) {
         throw new InvalidTokenException();
      }
   }

   @PostMapping("/register")
   public YouAppPrincipal register(
         @Valid @RequestBody AbstractPrincipal principal) {
      try {
         LOGGER.trace("Register: {}", principal);
         return authService.register(principal);
      }
      catch (Exception e) {
         LOGGER.error("Failed to register, due: {}", e.getMessage());
         throw new RegistrationException(e.getMessage());
      }
   }
}
