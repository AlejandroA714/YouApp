package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sv.com.udb.services.authentication.entities.*;
import sv.com.udb.services.authentication.enums.IRole;
import sv.com.udb.services.authentication.repository.IOAuthRegistrationRepository;
import sv.com.udb.services.authentication.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.repository.IPrivilegeRepository;
import sv.com.udb.services.authentication.repository.IRoleRepository;
import sv.com.udb.services.authentication.services.IAuthenticationService;
import sv.com.udb.services.authentication.services.IGoogleOAuth2Provider;
import sv.com.udb.services.authentication.services.IGoogleAuthenticationService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthenticationController {
   @NonNull
   private final IAuthenticationService       IAuthService;
   @NonNull
   private final IGoogleAuthenticationService IGoogleService;
   @NonNull
   private final IGoogleOAuth2Provider        googleOAuth2Provider;
   @NonNull
   private final IPrincipalRepository         principalRepository;
   @NonNull
   private final IRoleRepository              roleRepository;
   @NonNull
   private final IPrivilegeRepository         privilegeRepository;
   @NonNull
   private final IOAuthRegistrationRepository oAuthRegistrationRepository;

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

   @GetMapping("/users")
   public List<YouAppPrincipal> list() {
      var x = principalRepository.findAll();
      LOGGER.info(x.toString());
      return x;
   }

   @GetMapping("/roles")
   public Role role() {
      var x = roleRepository.findRoleByName(IRole.ROLE_USER);
      LOGGER.info(x.toString());
      return x;
   }

   @GetMapping("/privileges")
   public List<Privilege> privileges() {
      var x = privilegeRepository.findAll();
      LOGGER.info(x.toString());
      return x;
   }

   @GetMapping("/registration")
   public List<OAuthRegistrationType> registrationTypes() {
      var x = oAuthRegistrationRepository.findAll();
      LOGGER.info(x.toString());
      return x;
   }
}
