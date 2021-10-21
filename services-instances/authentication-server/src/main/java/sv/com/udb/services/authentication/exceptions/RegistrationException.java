package sv.com.udb.services.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RegistrationException extends ResponseStatusException {
   public RegistrationException(String message) {
      super(HttpStatus.BAD_REQUEST, "Failed to register due: " + message);
   }

   public RegistrationException(String message, Throwable cause) {
      super(HttpStatus.BAD_REQUEST, "Failed to register due: " + message,
            cause);
   }
}
