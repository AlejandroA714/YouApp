package sv.com.udb.services.authentication.oauth;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.NonNull;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwsEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.StringUtils;
import sv.com.udb.services.authentication.services.IOAuth2TokenService;

import java.util.Map;

public final class OAuth2ConfigurerUtils {
   private OAuth2ConfigurerUtils() {
   }

   public static <B extends HttpSecurityBuilder<B>> void IOAuth2TokenService(
         B builder, @NonNull IOAuth2TokenService tokenService) {
      builder.setSharedObject(IOAuth2TokenService.class, tokenService);
   }

   static <B extends HttpSecurityBuilder<B>> OAuth2AuthorizationService getAuthorizationService(
         B builder) {
      OAuth2AuthorizationService authorizationService = builder
            .getSharedObject(OAuth2AuthorizationService.class);
      if (authorizationService == null) {
         authorizationService = getOptionalBean(builder,
               OAuth2AuthorizationService.class);
         if (authorizationService == null) {
            authorizationService = new InMemoryOAuth2AuthorizationService();
         }
         builder.setSharedObject(OAuth2AuthorizationService.class,
               authorizationService);
      }
      return authorizationService;
   }

   static <B extends HttpSecurityBuilder<B>> RegisteredClientRepository getRegisteredClientRepository(
         B builder) {
      RegisteredClientRepository registeredClientRepository = builder
            .getSharedObject(RegisteredClientRepository.class);
      if (registeredClientRepository == null) {
         registeredClientRepository = getBean(builder,
               RegisteredClientRepository.class);
         builder.setSharedObject(RegisteredClientRepository.class,
               registeredClientRepository);
      }
      return registeredClientRepository;
   }

   static <B extends HttpSecurityBuilder<B>> IOAuth2TokenService getTokenService(
         B builder) {
      IOAuth2TokenService tokenService = builder
            .getSharedObject(IOAuth2TokenService.class);
      if (tokenService == null) {
         tokenService = getOptionalBean(builder, IOAuth2TokenService.class);
         builder.setSharedObject(IOAuth2TokenService.class, tokenService);
      }
      return tokenService;
   }

   static <B extends HttpSecurityBuilder<B>> JwtEncoder getJwtEncoder(
         B builder) {
      JwtEncoder jwtEncoder = builder.getSharedObject(JwtEncoder.class);
      if (jwtEncoder == null) {
         jwtEncoder = getOptionalBean(builder, JwtEncoder.class);
         if (jwtEncoder == null) {
            JWKSource<SecurityContext> jwkSource = getJwkSource(builder);
            jwtEncoder = new NimbusJwsEncoder(jwkSource);
         }
         builder.setSharedObject(JwtEncoder.class, jwtEncoder);
      }
      return jwtEncoder;
   }

   static <B extends HttpSecurityBuilder<B>> JWKSource<SecurityContext> getJwkSource(
         B builder) {
      JWKSource<SecurityContext> jwkSource = builder
            .getSharedObject(JWKSource.class);
      if (jwkSource == null) {
         ResolvableType type = ResolvableType
               .forClassWithGenerics(JWKSource.class, SecurityContext.class);
         jwkSource = getBean(builder, type);
         builder.setSharedObject(JWKSource.class, jwkSource);
      }
      return jwkSource;
   }

   static <B extends HttpSecurityBuilder<B>> OAuth2TokenCustomizer<JwtEncodingContext> getJwtCustomizer(
         B builder) {
      OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer = builder
            .getSharedObject(OAuth2TokenCustomizer.class);
      if (jwtCustomizer == null) {
         ResolvableType type = ResolvableType.forClassWithGenerics(
               OAuth2TokenCustomizer.class, JwtEncodingContext.class);
         jwtCustomizer = getOptionalBean(builder, type);
         if (jwtCustomizer != null) {
            builder.setSharedObject(OAuth2TokenCustomizer.class, jwtCustomizer);
         }
      }
      return jwtCustomizer;
   }

   static <B extends HttpSecurityBuilder<B>, T> T getBean(B builder,
         Class<T> type) {
      return builder.getSharedObject(ApplicationContext.class).getBean(type);
   }

   @SuppressWarnings("unchecked")
   static <B extends HttpSecurityBuilder<B>, T> T getBean(B builder,
         ResolvableType type) {
      ApplicationContext context = builder
            .getSharedObject(ApplicationContext.class);
      String[] names = context.getBeanNamesForType(type);
      if (names.length == 1) {
         return (T) context.getBean(names[0]);
      }
      if (names.length > 1) {
         throw new NoUniqueBeanDefinitionException(type, names);
      }
      throw new NoSuchBeanDefinitionException(type);
   }

   static <B extends HttpSecurityBuilder<B>, T> T getOptionalBean(B builder,
         Class<T> type) {
      Map<String, T> beansMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(
            builder.getSharedObject(ApplicationContext.class), type);
      if (beansMap.size() > 1) {
         throw new NoUniqueBeanDefinitionException(type, beansMap.size(),
               "Expected single matching bean of type '" + type.getName()
                     + "' but found " + beansMap.size() + ": "
                     + StringUtils.collectionToCommaDelimitedString(
                           beansMap.keySet()));
      }
      return (!beansMap.isEmpty() ? beansMap.values().iterator().next() : null);
   }

   @SuppressWarnings("unchecked")
   static <B extends HttpSecurityBuilder<B>, T> T getOptionalBean(B builder,
         ResolvableType type) {
      ApplicationContext context = builder
            .getSharedObject(ApplicationContext.class);
      String[] names = context.getBeanNamesForType(type);
      if (names.length > 1) {
         throw new NoUniqueBeanDefinitionException(type, names);
      }
      return names.length == 1 ? (T) context.getBean(names[0]) : null;
   }
}
