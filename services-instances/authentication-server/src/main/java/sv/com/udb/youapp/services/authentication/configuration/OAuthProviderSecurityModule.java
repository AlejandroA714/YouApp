package sv.com.udb.youapp.services.authentication.configuration;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import sv.com.udb.youapp.commons.jpa.dto.User;

public class OAuthProviderSecurityModule extends SimpleModule {
   public OAuthProviderSecurityModule() {
      super(OAuthProviderSecurityModule.class.getName(),
            new Version(1, 0, 0, null, null, null));
   }

   @Override
   public void setupModule(SetupContext context) {
      context.setMixInAnnotations(User.class, YouAppPrincipalMixIn.class);
   }
}
