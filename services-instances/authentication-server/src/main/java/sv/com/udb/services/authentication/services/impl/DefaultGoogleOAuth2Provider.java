package sv.com.udb.services.authentication.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import sv.com.udb.services.authentication.entities.GoogleAuthorizationRequest;
import sv.com.udb.services.authentication.entities.GooglePrincipal;
import sv.com.udb.services.authentication.exceptions.InvalidAuthenticationException;
import sv.com.udb.services.authentication.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.services.IGoogleOAuth2Provider;
import sv.com.udb.services.authentication.services.IGoogleAuthenticationService;
import sv.com.udb.services.authentication.services.IOAuth2TokenService;

@Slf4j
@RequiredArgsConstructor
public class DefaultGoogleOAuth2Provider implements IGoogleOAuth2Provider {
   @NonNull
   private final IGoogleAuthenticationService IGoogleService;
   @NonNull
   private final IPrincipalRepository         IPrincipalRepository;
   @NonNull
   private final IOAuth2TokenService          IOAuth2TokenService;

   @Override
   public Authentication authenticate(Authentication authentication)
         throws AuthenticationException {
      LOGGER.trace("Trying to authenticate: {}", authentication);
      if (!supports(authentication.getClass()))
         throw new InvalidAuthenticationException(authentication.getClass());
      try {
         GoogleAuthorizationRequest authRequest = (GoogleAuthorizationRequest) authentication;
         GoogleIdToken TOKEN = IGoogleService.validateToken(authRequest);
         return IOAuth2TokenService.getAcessToken(authentication);
      }
      catch (Exception e) {
         LOGGER.error("Failed to authenticate: {}", authentication, e);
         throw new InvalidAuthenticationException(e);
      }
   }

   @Override
   public boolean supports(Class<?> authentication) {
      return Authentication.class.isAssignableFrom(authentication);
   }
}
