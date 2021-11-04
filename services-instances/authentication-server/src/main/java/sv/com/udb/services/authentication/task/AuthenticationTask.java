package sv.com.udb.services.authentication.task;

import sv.com.udb.services.commons.models.Principal;

public interface AuthenticationTask extends Runnable {
   void setPrincipal(Principal principal);
}
