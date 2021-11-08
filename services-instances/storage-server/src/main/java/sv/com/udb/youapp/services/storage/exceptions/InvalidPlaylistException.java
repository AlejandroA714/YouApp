package sv.com.udb.youapp.services.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidPlaylistException extends ResponseStatusException {
   public InvalidPlaylistException(HttpStatus status) {
      super(status);
   }

   public InvalidPlaylistException() {
      super(HttpStatus.BAD_REQUEST, "That playlist does not exits");
   }
}
