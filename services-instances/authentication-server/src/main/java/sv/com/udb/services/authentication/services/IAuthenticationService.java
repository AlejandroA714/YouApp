package sv.com.udb.services.authentication.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import sv.com.udb.services.authentication.entities.AbstractPrincipal;
import sv.com.udb.services.authentication.entities.GoogleAuthorizationRequest;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.exceptions.InvalidTokenException;

public interface IAuthenticationService extends UserDetailsService {

   YouAppPrincipal register(AbstractPrincipal principal) throws Exception;

   void validateToken(String token) throws InvalidTokenException;
}
