package sv.com.udb.services.authentication.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import sv.com.udb.services.commons.models.AbstractPrincipal;

public class OAuthProviderSecurityModule extends SimpleModule {
   public OAuthProviderSecurityModule() {
      super(OAuthProviderSecurityModule.class.getName(),
            new Version(1, 0, 0, null, null, null));
   }

   @Override
   public void setupModule(SetupContext context) {
      context.setMixInAnnotations(AbstractPrincipal.class,
            AbstractPrincipalMixin.class);

   }
}
