package sv.com.udb.services.authentication.services.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import sv.com.udb.services.authentication.entities.AbstractPrincipal;
import sv.com.udb.services.authentication.entities.EmailToken;
import sv.com.udb.services.authentication.entities.GoogleAuthorizationRequest;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.exceptions.InvalidTokenException;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.authentication.repository.IEmailTokenRepository;
import sv.com.udb.services.authentication.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.services.IAuthenticationService;
import sv.com.udb.services.authentication.services.IEncryptionPasswordService;
import sv.com.udb.services.authentication.services.IGoogleOAuth2Provider;
import sv.com.udb.services.authentication.task.AuthenticationTask;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

@Slf4j
@RequiredArgsConstructor
public class DefaultAuthenticationService implements IAuthenticationService {
   @NonNull
   private final IPrincipalRepository       principalRepository;
   @NonNull
   private final IEncryptionPasswordService encryptionPasswordService;
   @NonNull
   private final AuthenticationProperties   properties;
   @NonNull
   private final ApplicationContext         context;
   @NonNull
   private final ExecutorService            executorService;
   @NonNull
   private final IEmailTokenRepository      tokenRepository;

   @Override
   public UserDetails loadUserByUsername(String s)
         throws UsernameNotFoundException {
      Optional<YouAppPrincipal> u = principalRepository.findByUsernameOrEmail(s,
            s);
      if (!u.isPresent())
         throw new UsernameNotFoundException(s + " could not be found");
      return u.get();
   }

   @Override
   public YouAppPrincipal register(AbstractPrincipal principal)
         throws Exception {
      YouAppPrincipal youAppPrincipal = YouAppPrincipal.from(principal);
      youAppPrincipal.setPassword(
            encryptionPasswordService.encryptPassword(principal.getPassword()));
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

   @Override
   public void validateToken(String token) throws InvalidTokenException {
      YouAppPrincipal principal;
      Optional<EmailToken> token_ = tokenRepository.getEmailTokenByToken(token);
      LOGGER.info("got: {}", token_);
      if (!token_.isPresent() || token_.get().getExpiration()
            .isAfter(LocalDateTime.now(ZoneId.of("GMT-06:00")))) {
         throw new InvalidTokenException();
      }
      principal = token_.get().getUser();
      principal.setIsActive(true);
      principalRepository.save(principal);
      LOGGER.info("Principal: {}", principal);
   }
}
