package sv.com.udb.services.authentication.task;

import sv.com.udb.services.authentication.entities.YouAppPrincipal;

public interface AuthenticationTask extends Runnable {
   void setPrincipal(YouAppPrincipal principal);
}
