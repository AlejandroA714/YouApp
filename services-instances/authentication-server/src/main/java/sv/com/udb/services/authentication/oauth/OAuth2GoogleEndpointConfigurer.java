package sv.com.udb.services.authentication.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import sv.com.udb.services.authentication.services.IGoogleAuthenticationService;
import sv.com.udb.services.authentication.services.impl.DefaultGoogleOAuth2Provider;

public class OAuth2GoogleEndpointConfigurer
      extends AbstractOAuth2ProviderConfigurer {
   private RequestMatcher requestMatcher;
   private final String   TOKEN_ENDPOINT = "/v1/auth/google";

   public OAuth2GoogleEndpointConfigurer(
         ObjectPostProcessor<Object> objectPostProcessor) {
      super(objectPostProcessor);
   }

   @Override
   protected <B extends HttpSecurityBuilder<B>> void init(B builder) {
      this.requestMatcher = new AntPathRequestMatcher(TOKEN_ENDPOINT,
            HttpMethod.POST.name());
      AuthenticationProvider authenticationProvider = new DefaultGoogleOAuth2Provider(
            OAuth2ConfigurerUtils.getBean(builder,
                  IGoogleAuthenticationService.class),
            OAuth2ConfigurerUtils.getTokenService(builder));
      builder.authenticationProvider(postProcess(authenticationProvider));
   }

   @Override
   protected <B extends HttpSecurityBuilder<B>> void configure(B builder) {
      AuthenticationManager authenticationManager = builder
            .getSharedObject(AuthenticationManager.class);
      ObjectMapper mapper = OAuth2ConfigurerUtils.getBean(builder,
            ObjectMapper.class);
      OAuth2GoogleEndpointFilter googleEndpointFilter = new OAuth2GoogleEndpointFilter(
            authenticationManager, TOKEN_ENDPOINT, mapper);
      builder.addFilterBefore(postProcess(googleEndpointFilter),
            AbstractPreAuthenticatedProcessingFilter.class);
   }

   @Override
   public RequestMatcher getRequestMatcher() {
      return this.requestMatcher;
   }
}
