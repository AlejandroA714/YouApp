package sv.com.udb.services.authentication.services;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface IGoogleOAuth2Provider {
   boolean supports(Class<?> authentication);

   Authentication authenticate(Authentication authentication)
         throws AuthenticationException;
}
