package sv.com.udb.services.authentication.exceptions;

import org.springframework.security.core.AuthenticationException;
import sv.com.udb.services.authentication.models.GoogleAuthorizationRequest;

public class InvalidAuthenticationException extends AuthenticationException {
   public InvalidAuthenticationException(Throwable cause) {
      super(cause.getMessage(), cause);
   }

   public InvalidAuthenticationException(String msg, Throwable cause) {
      super(msg, cause);
   }

   public InvalidAuthenticationException(Class<?> tClass) {
      super(String.format("%s is not assignable from %s", tClass,
            GoogleAuthorizationRequest.class));
   }
}
