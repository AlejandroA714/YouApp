package sv.com.udb.services.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidCredentialsException extends ResponseStatusException {
   public InvalidCredentialsException(HttpStatus status) {
      super(status);
   }

   public InvalidCredentialsException(String status, Throwable e) {
      super(HttpStatus.BAD_REQUEST, status, e);
   }

   public InvalidCredentialsException(String status) {
      super(HttpStatus.BAD_REQUEST, status);
   }
}
