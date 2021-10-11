package sv.com.udb.services.authentication.task;

import lombok.NonNull;
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
public class SendEmailTask implements AuthenticationTask {
   @NonNull
   private YouAppPrincipal principal;

   @Override
   public void setPrincipal(YouAppPrincipal principal) {
      this.principal = principal;
   }

   @Override
   public void run() {
      /*
       * getOnboardingProperties().getPostCreationTasks().parallelStream()
       * .forEach(c -> { try { var clazz = Class.forName(c); var t =
       * (OnboardingTask) getApplicationContext() .getBean(clazz);
       * t.setData(getOnboardingProperties(), request, finalCreateUserResponse,
       * node); getExecutorService().submit(t); } catch (Exception e) {
       * LOGGER.error("Ocurrio un error", e); } });
       */
   }
}
