package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.com.udb.services.authentication.entities.GoogleAuthorizationRequest;
import sv.com.udb.services.authentication.services.IAuthenticationService;
import sv.com.udb.services.authentication.services.IGoogleOAuth2Provider;
import sv.com.udb.services.authentication.services.IGoogleAuthenticationService;

import javax.validation.Valid;

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

   @PostMapping("/google")
   public OAuth2AccessToken google(
         @Valid @RequestBody GoogleAuthorizationRequest principal) {
      try {
         var x = googleOAuth2Provider.authenticate(principal);
         var y = ((OAuth2AccessTokenAuthenticationToken) x).getAccessToken();
         return y;
      }
      catch (Exception e) {
         throw e;
      }
   }
}
