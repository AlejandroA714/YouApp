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
   private final IGoogleOAuth2Provider      googleOAuth2Provider;
   @NonNull
   private final IPrincipalRepository       principalRepository;
   @NonNull
   private final IAuthenticationService     authService;
   @NonNull
   private final IEncryptionPasswordService encryptionPasswordService;
   @NonNull
   private final AuthenticationProperties   properties;
   @NonNull
   private final ApplicationContext         context;
   @NonNull
   private final ExecutorService            executorService;

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
   public YouAppPrincipal register(
         @Valid @RequestBody AbstractPrincipal principal) {
      try {
         YouAppPrincipal youAppPrincipal = YouAppPrincipal.from(principal);
         youAppPrincipal.setPassword(encryptionPasswordService
               .encryptPassword(principal.getPassword()));
         String base64photo = youAppPrincipal.getPhoto();
         youAppPrincipal.setPhoto(null);
         principalRepository.save(youAppPrincipal);
         youAppPrincipal.setPhoto(base64photo);
         properties.getPostCreationTasks().parallelStream().forEach(Class -> {
            AuthenticationTask task = context.getBean(Class);
            task.setPrincipal(youAppPrincipal);
            executorService.submit(task);
         });
         return youAppPrincipal;
      }
      catch (Exception e) {
         LOGGER.error("Failed to register, due: {}", e.getMessage());
         throw new RegistrationException(e.getMessage());
      }
   }

   @GetMapping(value = "/getFile", produces = MediaType.IMAGE_JPEG_VALUE)
   public byte[] getFile() {
      try {
         // var x = minioService
         // .getFile("566a1985-bcd3-45c6-bdd4-539e653d2607suffix.jpg");
         return new byte[0];
      }
      catch (Exception e) {
         return null;
      }
   }

   @GetMapping("/list")
   public List<YouAppPrincipal> principal() {
      return principalRepository.findAll();
   }

   @GetMapping("/test")
   public void test() throws Exception {
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
