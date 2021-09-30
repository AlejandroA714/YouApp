package sv.com.udb.services.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidTokenException extends ResponseStatusException {
   public InvalidTokenException() {
      super(HttpStatus.BAD_REQUEST, "Invalid token");
   }
}
