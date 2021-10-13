package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sv.com.udb.services.authentication.entities.*;
import sv.com.udb.services.authentication.enums.IOAuthRegistrationType;
import sv.com.udb.services.authentication.exceptions.RegistrationException;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.authentication.repository.IOAuthRegistrationRepository;
import sv.com.udb.services.authentication.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.services.IEncryptionPasswordService;
import sv.com.udb.services.authentication.services.IGoogleOAuth2Provider;
import sv.com.udb.services.authentication.task.AuthenticationTask;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthenticationController {
   @NonNull
   private final IGoogleOAuth2Provider        googleOAuth2Provider;
   @NonNull
   private final IPrincipalRepository         principalRepository;
   @NonNull
   private final IOAuthRegistrationRepository oAuthRegistrationRepository;
   @NonNull
   private final IEncryptionPasswordService   encryptionPasswordService;
   @NonNull
   private final AuthenticationProperties     properties;
   @NonNull
   private final ApplicationContext           context;
   @NonNull
   private final ExecutorService              executorService;

   @PostMapping("/google")
   public OAuth2AccessToken google(
         @Valid @RequestBody GoogleAuthorizationRequest principal) {
      try {
         Authentication auth = googleOAuth2Provider.authenticate(principal);
         return ((OAuth2AccessTokenAuthenticationToken) auth).getAccessToken();
      }
      catch (Exception e) {
         throw e;
      }
   }

   @PostMapping("/register")
   public void register(@Valid @RequestBody AbstractPrincipal principal) {
      try {
         YouAppPrincipal youAppPrincipal = YouAppPrincipal.from(principal);
         youAppPrincipal.setPassword(encryptionPasswordService
               .encryptPassword(principal.getPassword()));
         principalRepository.save(youAppPrincipal);
      }
      catch (Exception e) {
         LOGGER.error("Failed to register, due: {}", e.getMessage());
         throw new RegistrationException(e.getMessage());
      }
   }

   @GetMapping("/list")
   public List<YouAppPrincipal> principal() {
      return principalRepository.findAll();
   }

   @GetMapping("/call")
   public void call() {
      properties.getPostCreationTasks().parallelStream().forEach(Class -> {
         AuthenticationTask task = context.getBean(Class);
         // task.setPrincipal(YouAppPrincipal.builder().nombres("Alejandro
         // Alejo").email("alejandroalejo714@gmail.com")
         // .birthday(LocalDate.of(2020,07,14))
         // .username("alejandroalejo").build());
         executorService.submit(task);
      });
   }
}
