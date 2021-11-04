package sv.com.udb.services.authentication.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import sv.com.udb.services.commons.models.AbstractPrincipal;
import sv.com.udb.services.commons.entities.YouAppPrincipal;

public interface IAuthenticationService extends UserDetailsService {
   YouAppPrincipal register(AbstractPrincipal principal) throws Exception;

   String validateToken(String token);
}
