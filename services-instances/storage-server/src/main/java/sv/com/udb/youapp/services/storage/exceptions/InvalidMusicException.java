package sv.com.udb.youapp.services.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidMusicException extends ResponseStatusException {
   public InvalidMusicException(HttpStatus status) {
      super(status);
   }

   public InvalidMusicException() {
      super(HttpStatus.BAD_REQUEST, "That song does not exits");
   }
}
