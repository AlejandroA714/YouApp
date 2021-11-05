package sv.com.udb.services.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidAuthenticationException extends ResponseStatusException {
   public InvalidAuthenticationException(Throwable cause) {
      super(HttpStatus.BAD_REQUEST, "Failed to login with google", cause);
   }

   public InvalidAuthenticationException(String message, Throwable cause) {
      super(HttpStatus.BAD_REQUEST, message, cause);
   }

   public InvalidAuthenticationException(String message) {
      super(HttpStatus.BAD_REQUEST, message);
   }
}
