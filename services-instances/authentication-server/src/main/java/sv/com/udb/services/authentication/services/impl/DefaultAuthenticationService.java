package sv.com.udb.services.authentication.services.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import sv.com.udb.components.mail.sender.services.IEmailService;
import sv.com.udb.services.commons.exceptions.PrincipalDoesNotExist;
import sv.com.udb.services.commons.models.AbstractPrincipal;
import sv.com.udb.services.commons.entities.EmailToken;
import sv.com.udb.services.commons.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.commons.repository.IEmailTokenRepository;
import sv.com.udb.services.commons.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.services.IAuthenticationService;
import sv.com.udb.services.authentication.services.IEncryptionPasswordService;
import sv.com.udb.services.authentication.task.AuthenticationTask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
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
   @NonNull
   private final IEmailService              emailService;
   private static final String              SUCCESS = "success";

   @Override
   @Transactional
   public UserDetails loadUserByUsername(String s)
         throws UsernameNotFoundException {
      Optional<YouAppPrincipal> u = principalRepository
            .findByUsernameOrEmail(s);
      if (!u.isPresent())
         throw new UsernameNotFoundException(s + " could not be found");
      return AbstractPrincipal.from(u.get());
   }

   @Override
   public YouAppPrincipal register(AbstractPrincipal principal)
         throws Exception {
      YouAppPrincipal youAppPrincipal = YouAppPrincipal.from(principal);
      youAppPrincipal.setPassword(
            encryptionPasswordService.encryptPassword(principal.getPassword()));
      youAppPrincipal.setPhoto(null);
      youAppPrincipal.setRegistration(LocalDate.now(ZoneId.of("GMT-06:00")));
      principalRepository.save(youAppPrincipal);
      properties.getPostCreationTasks().parallelStream().forEach(Class -> {
         AuthenticationTask task = context.getBean(Class);
         task.setPrincipal(principal);
         executorService.submit(task);
      });
      return youAppPrincipal;
   }

   @Override
   public String validateToken(String token) {
      YouAppPrincipal principal;
      Optional<EmailToken> token_ = tokenRepository.getEmailTokenByToken(token);
      Map<String, Object> props = new HashMap<>();
      LOGGER.info("got: {}", token_);
      if (!token_.isPresent() || token_.get().getExpiration()
            .isBefore(LocalDateTime.now(ZoneId.of("GMT-06:00")))) {
         props.put(SUCCESS, false);
      }
      else {
         principal = token_.get().getUser();
         principal.setIsActive(true);
         principalRepository.save(principal);
         LOGGER.info("User: {} has been activated", principal.getId());
         props.put(SUCCESS, true);
         props.putAll(principal.getSummary());
      }
      return emailService.processTemplate("confirmed_mail.html", props);
   }

   @Override
   public YouAppPrincipal me(String uuid) {
      Optional<YouAppPrincipal> principal = principalRepository.findById(uuid);
      if (!principal.isPresent()) {
         throw new PrincipalDoesNotExist(uuid + " does not exits");
      }
      return principal.get();
   }
}
