package sv.com.udb.youapp.services.authentication.services.impl;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.util.Assert;
import sv.com.udb.youapp.services.authentication.entities.RegisteredClientEntity;
import sv.com.udb.youapp.services.authentication.repositories.JpaClientRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DefaultRegisteredClientRepository implements
      org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository {
   @NonNull
   private final JpaClientRepository jpaClientRepository;
   @NonNull
   private final ObjectMapper        objectMapper = new ObjectMapper();

   public DefaultRegisteredClientRepository(
         JpaClientRepository jpaClientRepository) {
      this.jpaClientRepository = jpaClientRepository;
      ClassLoader classLoader = DefaultRegisteredClientRepository.class
            .getClassLoader();
      List<Module> securityModules = SecurityJackson2Modules
            .getModules(classLoader);
      this.objectMapper.registerModules(securityModules);
      this.objectMapper
            .registerModule(new OAuth2AuthorizationServerJackson2Module());
   }

   @Override
   public void save(RegisteredClient registeredClient) {
      Assert.notNull(registeredClient, "registeredClient cannot be null");
      this.jpaClientRepository.save(toEntity(registeredClient));
   }

   @Override
   public RegisteredClient findById(String id) {
      Assert.hasText(id, "id cannot be empty");
      return this.jpaClientRepository.findById(id).map(this::toObject)
            .orElse(null);
   }

   @Override
   public RegisteredClient findByClientId(String clientId) {
      Assert.hasText(clientId, "clientId cannot be empty");
      return jpaClientRepository.findByClientId(clientId).map(this::toObject)
            .orElse(null);
   }

   private RegisteredClient toObject(RegisteredClientEntity entity) {
      LOGGER.info("mapping ....");
      RegisteredClient.Builder builder = RegisteredClient.withId(entity.getId())
            .clientId(entity.getClientId())
            .clientIdIssuedAt(entity.getClientIssuedAt())
            .clientSecret(entity.getClientSecret())
            .clientSecretExpiresAt(entity.getClientSecretExpiresAt())
            .clientName(entity.getClientName())
            .clientAuthenticationMethods(entity::clientMethods)
            .authorizationGrantTypes(entity::grantTypes)
            .redirectUris(entity::redirectUris).scopes(entity::scopes);
      // Map<String, Object> clientSettingsMap = parseMap(
      // entity.getClientSettings());
      // builder.clientSettings(
      // ClientSettings.withSettings(clientSettingsMap).build());
      // Map<String, Object> tokenSettingsMap = parseMap(
      // entity.getTokenSettings());
      // builder.tokenSettings(
      // TokenSettings.withSettings(tokenSettingsMap).build());
      return builder.build();
   }

   private RegisteredClientEntity toEntity(RegisteredClient registeredClient) {
      List<String> clientAuthenticationMethods = new ArrayList<>(
            registeredClient.getClientAuthenticationMethods().size());
      registeredClient.getClientAuthenticationMethods()
            .forEach(clientAuthenticationMethod -> clientAuthenticationMethods
                  .add(clientAuthenticationMethod.getValue()));
      List<String> authorizationGrantTypes = new ArrayList<>(
            registeredClient.getAuthorizationGrantTypes().size());
      registeredClient.getAuthorizationGrantTypes()
            .forEach(authorizationGrantType -> authorizationGrantTypes
                  .add(authorizationGrantType.getValue()));
      RegisteredClientEntity entity = new RegisteredClientEntity();
      entity.setId(registeredClient.getId());
      entity.setClientId(registeredClient.getClientId());
      entity.setClientIssuedAt(registeredClient.getClientIdIssuedAt());
      entity.setClientSecret(registeredClient.getClientSecret());
      entity.setClientSecretExpiresAt(
            registeredClient.getClientSecretExpiresAt());
      entity.setClientName(registeredClient.getClientName());
      // entity.setClientAuthenticationMethods(StringUtils
      // .collectionToCommaDelimitedString(clientAuthenticationMethods));
      // entity.setAuthorizationGrantTypes(StringUtils
      // .collectionToCommaDelimitedString(authorizationGrantTypes));
      // entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(
      // registeredClient.getRedirectUris()));
      // entity.setScopes(StringUtils
      // .collectionToCommaDelimitedString(registeredClient.getScopes()));
      // entity.setClientSettings(
      // writeMap(registeredClient.getClientSettings().getSettings()));
      // entity.setTokenSettings(
      // writeMap(registeredClient.getTokenSettings().getSettings()));
      return entity;
   }

   private static AuthorizationGrantType resolveAuthorizationGrantType(
         String authorizationGrantType) {
      if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue()
            .equals(authorizationGrantType)) {
         return AuthorizationGrantType.AUTHORIZATION_CODE;
      }
      else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()
            .equals(authorizationGrantType)) {
         return AuthorizationGrantType.CLIENT_CREDENTIALS;
      }
      else if (AuthorizationGrantType.REFRESH_TOKEN.getValue()
            .equals(authorizationGrantType)) {
         return AuthorizationGrantType.REFRESH_TOKEN;
      }
      return new AuthorizationGrantType(authorizationGrantType);
   }

   private static ClientAuthenticationMethod resolveClientAuthenticationMethod(
         String clientAuthenticationMethod) {
      if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()
            .equals(clientAuthenticationMethod)) {
         return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
      }
      else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue()
            .equals(clientAuthenticationMethod)) {
         return ClientAuthenticationMethod.CLIENT_SECRET_POST;
      }
      else if (ClientAuthenticationMethod.NONE.getValue()
            .equals(clientAuthenticationMethod)) {
         return ClientAuthenticationMethod.NONE;
      }
      return new ClientAuthenticationMethod(clientAuthenticationMethod);
   }
}
