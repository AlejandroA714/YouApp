package sv.com.udb.services.authentication.task;

import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.models.Principal;

public interface AuthenticationTask extends Runnable {
   void setPrincipal(Principal principal);
}
