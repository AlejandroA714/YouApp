package sv.com.udb.services.authentication.services.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import sv.com.udb.services.authentication.exceptions.InvalidAuthenticationException;
import sv.com.udb.services.authentication.models.GoogleAuthorizationRequest;
import sv.com.udb.services.authentication.services.IGoogleOAuth2Provider;
import sv.com.udb.services.authentication.services.IGoogleAuthenticationService;
import sv.com.udb.services.authentication.services.IOAuth2TokenService;

@Slf4j
@RequiredArgsConstructor
public class DefaultGoogleOAuth2Provider implements IGoogleOAuth2Provider {
   @NonNull
   private final IGoogleAuthenticationService IGoogleService;
   @NonNull
   private final IOAuth2TokenService          IOAuth2TokenService;

   @Override
   public Authentication authenticate(Authentication authentication)
         throws AuthenticationException {
      LOGGER.trace("Trying to authenticate: {}", authentication);
      try {
         GoogleAuthorizationRequest authRequest = (GoogleAuthorizationRequest) authentication;
         IGoogleService.validateToken(authRequest);
         IGoogleService.registerIfNotExits(authRequest);
         OAuth2AccessTokenAuthenticationToken acessToken = IOAuth2TokenService
               .getAcessToken(authentication);
         return acessToken;
      }
      catch (Exception e) {
         LOGGER.error("Failed to authenticate: {}", authentication, e);
         throw new InvalidAuthenticationException(e);
      }
   }

   @Override
   public boolean supports(Class<?> authentication) {
      return GoogleAuthorizationRequest.class.isAssignableFrom(authentication);
   }
}
