package sv.com.udb.services.authentication.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import sv.com.udb.services.authentication.models.AbstractPrincipal;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.exceptions.InvalidTokenException;

public interface IAuthenticationService extends UserDetailsService {
   YouAppPrincipal register(AbstractPrincipal principal) throws Exception;

   String validateToken(String token);
}
