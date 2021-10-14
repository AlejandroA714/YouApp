package sv.com.udb.services.authentication.oauth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import sv.com.udb.services.authentication.services.IOAuth2TokenService;
import sv.com.udb.services.authentication.services.impl.DefaultOAuth2TokenService;

import java.util.LinkedHashMap;
import java.util.Map;

public class OAuth2ProviderConfigurer<B extends HttpSecurityBuilder<B>>
      extends AbstractHttpConfigurer<OAuth2ProviderConfigurer<B>, B> {
   private final Map<Class<? extends AbstractOAuth2ProviderConfigurer>, AbstractOAuth2ProviderConfigurer> configurers      = createConfigurers();
   @Getter
   private final RequestMatcher                                                                           endpointsMatcher = request -> {
                                                                                                                              var match = getRequestMatcher(
                                                                                                                                    OAuth2GoogleEndpointConfigurer.class)
                                                                                                                                          .matches(
                                                                                                                                                request);
                                                                                                                              return match;
                                                                                                                           };

   private Map<Class<? extends AbstractOAuth2ProviderConfigurer>, AbstractOAuth2ProviderConfigurer> createConfigurers() {
      Map<Class<? extends AbstractOAuth2ProviderConfigurer>, AbstractOAuth2ProviderConfigurer> configurers = new LinkedHashMap<>();
      configurers.put(OAuth2GoogleEndpointConfigurer.class,
            new OAuth2GoogleEndpointConfigurer(this::postProcess));
      return configurers;
   }

   @Override
   public void init(B builder) {
      IOAuth2TokenService tokenService = new DefaultOAuth2TokenService(
            OAuth2ConfigurerUtils.getRegisteredClientRepository(builder),
            OAuth2ConfigurerUtils.getBean(builder, ProviderSettings.class),
            OAuth2ConfigurerUtils.getAuthorizationService(builder),
            OAuth2ConfigurerUtils.getJwtEncoder(builder));
      OAuth2ConfigurerUtils.IOAuth2TokenService(builder, tokenService);
      this.configurers.values().forEach(configurer -> configurer.init(builder));
      ExceptionHandlingConfigurer<B> exceptionHandling = builder
            .getConfigurer(ExceptionHandlingConfigurer.class);
      if (exceptionHandling != null) {
         exceptionHandling.defaultAuthenticationEntryPointFor(
               new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
               new OrRequestMatcher(
                     getRequestMatcher(OAuth2GoogleEndpointConfigurer.class)));
      }
   }

   @Override
   public void configure(B builder) {
      this.configurers.values()
            .forEach(configurer -> configurer.configure(builder));
   }

   private <T extends AbstractOAuth2ProviderConfigurer> RequestMatcher getRequestMatcher(
         Class<T> configurerType) {
      return getConfigurer(configurerType).getRequestMatcher();
   }

   private <T> T getConfigurer(Class<T> type) {
      return (T) this.configurers.get(type);
   }
}
