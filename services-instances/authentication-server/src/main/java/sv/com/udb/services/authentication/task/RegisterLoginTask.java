package sv.com.udb.services.authentication.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;

@Slf4j
@Component
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegisterLoginTask implements AuthenticationTask {
   @Override
   public void setPrincipal(YouAppPrincipal principal) {
   }

   @Override
   public void run() {
   }
}
