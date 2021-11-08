package sv.com.udb.youapp.services.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidGenereException extends ResponseStatusException {
   public InvalidGenereException(HttpStatus status) {
      super(status);
   }

   public InvalidGenereException() {
      super(HttpStatus.BAD_REQUEST, "That genre does not exits");
   }
}
