package sv.com.udb.youapp.services.authentication.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sv.com.udb.youapp.services.authentication.configuration.OAuthProviderSecurityModule;
import sv.com.udb.youapp.services.authentication.entities.AccessTokenMetadataEntity;
import sv.com.udb.youapp.services.authentication.entities.AuthorizationCodeMetadataEntity;
import sv.com.udb.youapp.services.authentication.entities.AuthorizationEntity;
import sv.com.udb.youapp.services.authentication.repositories.JpaAuthorizationRepository;
import sv.com.udb.youapp.services.authentication.repositories.ScopeRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class DefaultAuthorizationService implements OAuth2AuthorizationService {
   @NonNull
   private final JpaAuthorizationRepository authorizationRepository;
   @NonNull
   private final RegisteredClientRepository registeredClientRepository;
   @NonNull
   private final ScopeRepository            scopeRepository;
   private final ObjectMapper               objectMapper = new ObjectMapper();

   public DefaultAuthorizationService(
         JpaAuthorizationRepository jpaAuthorizationRepository,
         RegisteredClientRepository registeredClientRepository,
         ScopeRepository scopeRepository) {
      this.authorizationRepository = jpaAuthorizationRepository;
      this.registeredClientRepository = registeredClientRepository;
      this.scopeRepository = scopeRepository;
      ClassLoader classLoader = DefaultAuthorizationService.class
            .getClassLoader();
      List<Module> securityModules = SecurityJackson2Modules
            .getModules(classLoader);
      this.objectMapper.registerModule(new OAuthProviderSecurityModule());
      this.objectMapper.registerModules(securityModules);
      this.objectMapper
            .registerModule(new OAuth2AuthorizationServerJackson2Module());
   }

   @Override
   public void save(OAuth2Authorization authorization) {
      this.authorizationRepository.save(toEntity(authorization));
   }

   @Override
   public void remove(OAuth2Authorization authorization) {
      Assert.notNull(authorization, "authorization cannot be null");
      this.authorizationRepository.deleteById(authorization.getId());
   }

   @Override
   public OAuth2Authorization findById(String id) {
      Assert.hasText(id, "id cannot be empty");
      return this.authorizationRepository.findById(id).map(this::toObject)
            .orElse(null);
   }

   @Override
   public OAuth2Authorization findByToken(String token,
         OAuth2TokenType tokenType) {
      Assert.hasText(token, "token cannot be empty");
      Optional<AuthorizationEntity> result;
      if (tokenType == null) {
         result = this.authorizationRepository
               .findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValue(
                     token);
      }
      else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
         result = this.authorizationRepository.findByState(token);
      }
      else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
         result = this.authorizationRepository
               .findByAuthorizationCodeValue(token);
      }
      else if (OAuth2ParameterNames.ACCESS_TOKEN.equals(tokenType.getValue())) {
         result = this.authorizationRepository.findByAccessTokenValue(token);
      }
      else if (OAuth2ParameterNames.REFRESH_TOKEN
            .equals(tokenType.getValue())) {
         result = this.authorizationRepository.findByRefreshTokenValue(token);
      }
      else {
         result = Optional.empty();
      }
      return result.map(this::toObject).orElse(null);
   }

   private OAuth2Authorization toObject(AuthorizationEntity entity) {
      RegisteredClient registeredClient = this.registeredClientRepository
            .findById(entity.getRegisteredClientId());
      if (registeredClient == null) {
         throw new DataRetrievalFailureException(
               "The RegisteredClient with id '" + entity.getRegisteredClientId()
                     + "' was not found in the RegisteredClientRepository.");
      }
      OAuth2Authorization.Builder builder = OAuth2Authorization
            .withRegisteredClient(registeredClient).id(entity.getId())
            .principalName(entity.getPrincipalName())
            .authorizationGrantType(resolveAuthorizationGrantType(
                  entity.getAuthorizationGrantType()))
            .authorizedScopes(StringUtils
                  .commaDelimitedListToSet(entity.getAuthorizedScopes()))
            .attributes(attributes -> attributes
                  .putAll(parseMap(entity.getAttributes())));
      if (entity.getState() != null) {
         builder.attribute(OAuth2ParameterNames.STATE, entity.getState());
      }
      if (entity.getAuthorizationCodeValue() != null) {
         OAuth2AuthorizationCode authorizationCode = new OAuth2AuthorizationCode(
               entity.getAuthorizationCodeValue(),
               entity.getAuthorizationCodeIssuedAt(),
               entity.getAuthorizationCodeExpiresAt());
         builder.token(authorizationCode, metadata -> metadata
               .putAll(entity.getAuthorizationCodeMetadata().toPOJO()));
      }
      if (entity.getAccessTokenValue() != null) {
         OAuth2AccessToken accessToken = new OAuth2AccessToken(
               OAuth2AccessToken.TokenType.BEARER, entity.getAccessTokenValue(),
               entity.getAccessTokenIssuedAt(),
               entity.getAccessTokenExpiresAt(), StringUtils
                     .commaDelimitedListToSet(entity.getAccessTokenScopes()));
         // builder.token(accessToken, metadata -> metadata
         // .putAll(parseMap(entity.getAccessTokenMetadata())));
      }
      if (entity.getRefreshTokenValue() != null) {
         OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
               entity.getRefreshTokenValue(), entity.getRefreshTokenIssuedAt(),
               entity.getRefreshTokenExpiresAt());
         builder.token(refreshToken, metadata -> metadata
               .putAll(parseMap(entity.getRefreshTokenMetadata())));
      }
      if (entity.getOidcIdTokenValue() != null) {
         OidcIdToken idToken = new OidcIdToken(entity.getOidcIdTokenValue(),
               entity.getOidcIdTokenIssuedAt(),
               entity.getOidcIdTokenExpiresAt(),
               parseMap(entity.getOidcIdTokenClaims()));
         builder.token(idToken, metadata -> metadata
               .putAll(parseMap(entity.getOidcIdTokenMetadata())));
      }
      return builder.build();
   }

   private AuthorizationEntity toEntity(OAuth2Authorization authorization) {
      AuthorizationEntity entity = new AuthorizationEntity();
      entity.setId(authorization.getId());
      entity.setRegisteredClientId(authorization.getRegisteredClientId());
      entity.setPrincipalName(authorization.getPrincipalName());
      entity.setAuthorizationGrantType(
            authorization.getAuthorizationGrantType().getValue());
      entity.setAuthorizedScopes(StringUtils.collectionToCommaDelimitedString(
            authorization.getAuthorizedScopes()));
      entity.setAttributes(writeMap(authorization.getAttributes()));
      entity.setState(authorization.getAttribute(OAuth2ParameterNames.STATE));
      // Authorization Code
      OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
            .getToken(OAuth2AuthorizationCode.class);
      setTokenValues(authorizationCode, entity::setAuthorizationCodeValue,
            entity::setAuthorizationCodeIssuedAt,
            entity::setAuthorizationCodeExpiresAt,
            entity::setAuthorizationCodeMetadata,
            AuthorizationCodeMetadataEntity::create);
      // Access Token
      OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization
            .getToken(OAuth2AccessToken.class);
      setTokenValues(accessToken, entity::setAccessTokenValue,
            entity::setAccessTokenIssuedAt, entity::setAccessTokenExpiresAt,
            entity::setAccessTokenMetadata, AccessTokenMetadataEntity::create);
      // Set Metadata
      if (accessToken != null && accessToken.getToken().getScopes() != null) {
         entity.setAccessTokenScopes(
               StringUtils.collectionToCommaDelimitedString(
                     accessToken.getToken().getScopes()));
      }
      // Refresh Token
      OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization
            .getToken(OAuth2RefreshToken.class);
      setTokenValues(refreshToken, entity::setRefreshTokenValue,
            entity::setRefreshTokenIssuedAt, entity::setRefreshTokenExpiresAt,
            entity::setRefreshTokenMetadata,
            stringObjectMap -> writeMap(stringObjectMap));
      // Oidc
      OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization
            .getToken(OidcIdToken.class);
      setTokenValues(oidcIdToken, entity::setOidcIdTokenValue,
            entity::setOidcIdTokenIssuedAt, entity::setOidcIdTokenExpiresAt,
            entity::setOidcIdTokenMetadata,
            stringObjectMap -> writeMap(stringObjectMap));
      if (oidcIdToken != null) {
         entity.setOidcIdTokenClaims(writeMap(oidcIdToken.getClaims()));
      }
      return entity;
   }

   private <R> void setTokenValues(OAuth2Authorization.Token<?> token,
         Consumer<String> tokenValueConsumer,
         Consumer<Instant> issuedAtConsumer,
         Consumer<Instant> expiresAtConsumer, Consumer<R> tokenMetadataConsumer,
         Function<Map<String, Object>, R> tokenProcessor) {
      if (token != null) {
         OAuth2Token oAuth2Token = token.getToken();
         tokenValueConsumer.accept(oAuth2Token.getTokenValue());
         issuedAtConsumer.accept(oAuth2Token.getIssuedAt());
         expiresAtConsumer.accept(oAuth2Token.getExpiresAt());
         var payload = tokenProcessor.apply(token.getMetadata());
         tokenMetadataConsumer.accept(payload);
      }
   }

   private Map<String, Object> parseMap(String data) {
      try {
         return this.objectMapper.readValue(data,
               new TypeReference<Map<String, Object>>() {
               });
      }
      catch (Exception ex) {
         throw new IllegalArgumentException(ex.getMessage(), ex);
      }
   }

   private String writeMap(Map<String, Object> metadata) {
      try {
         return this.objectMapper.writeValueAsString(metadata);
      }
      catch (Exception ex) {
         throw new IllegalArgumentException(ex.getMessage(), ex);
      }
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
}
