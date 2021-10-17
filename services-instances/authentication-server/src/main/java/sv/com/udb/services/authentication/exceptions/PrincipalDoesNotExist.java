package sv.com.udb.services.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PrincipalDoesNotExist extends ResponseStatusException {
   public PrincipalDoesNotExist(String message) {
      super(HttpStatus.NOT_FOUND, "User: " + message + " doest not exist");
   }
}
