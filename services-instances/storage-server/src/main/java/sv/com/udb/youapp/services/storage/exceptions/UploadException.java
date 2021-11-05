package sv.com.udb.youapp.services.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UploadException extends ResponseStatusException {
   public UploadException(HttpStatus status) {
      super(status);
   }

   public UploadException(String message, Throwable cause) {
      super(HttpStatus.BAD_REQUEST, message, cause);
   }
}
