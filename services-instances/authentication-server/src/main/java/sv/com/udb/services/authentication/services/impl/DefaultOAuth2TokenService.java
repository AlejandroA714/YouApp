package sv.com.udb.services.authentication.services.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JoseHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sv.com.udb.services.authentication.services.IOAuth2TokenService;

import java.security.Principal;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class DefaultOAuth2TokenService implements IOAuth2TokenService {
   @NonNull
   private final RegisteredClientRepository                clientRepository;
   @NonNull
   private final ProviderSettings                          providerSettings;
   @NonNull
   private final OAuth2AuthorizationService                authorizationService;
   @NonNull
   private final JwtEncoder                                jwtEncoder;
   private final OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer                   = (
         context) -> {
                                                                                           };
   private static final StringKeyGenerator                 DEFAULT_REFRESH_TOKEN_GENERATOR = new Base64StringKeyGenerator(
         Base64.getUrlEncoder().withoutPadding(), 96);
   private Supplier<String>                                refreshTokenGenerator           = DEFAULT_REFRESH_TOKEN_GENERATOR::generateKey;
   private final String                                    CLIENT_ID                       = "youapp";

   @Override
   public OAuth2AccessTokenAuthenticationToken getAcessToken(
         Authentication auth) {
      RegisteredClient registeredClient = clientRepository
            .findByClientId(CLIENT_ID);
      String issuer = this.providerSettings.getIssuer();
      Set<String> authorizedScopes = Collections.singleton("openid");
      JoseHeader.Builder headersBuilder = JoseHeader
            .withAlgorithm(SignatureAlgorithm.RS256);
      JwtClaimsSet.Builder claimsBuilder = accessTokenClaims(registeredClient,
            issuer, auth.getName(), authorizedScopes);
      JwtEncodingContext context = JwtEncodingContext
            .with(headersBuilder, claimsBuilder)
            .registeredClient(registeredClient).principal(auth)
            .authorizedScopes(authorizedScopes)
            .tokenType(OAuth2TokenType.ACCESS_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .build();
      this.jwtCustomizer.customize(context);
      JoseHeader headers = context.getHeaders().build();
      JwtClaimsSet claims = context.getClaims().build();
      Jwt jwtAccessToken = this.jwtEncoder.encode(headers, claims);
      OAuth2AccessToken accessToken = new OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER, jwtAccessToken.getTokenValue(),
            jwtAccessToken.getIssuedAt(), jwtAccessToken.getExpiresAt(),
            authorizedScopes);
      OAuth2RefreshToken refreshToken = null;
      if (registeredClient.getAuthorizationGrantTypes()
            .contains(AuthorizationGrantType.REFRESH_TOKEN)) {
         refreshToken = generateRefreshToken(
               registeredClient.getTokenSettings().getRefreshTokenTimeToLive());
      }
      OAuth2Authorization authorization = OAuth2Authorization
            .withRegisteredClient(registeredClient).accessToken(accessToken)
            .refreshToken(refreshToken).principalName(auth.getName())
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .attribute("test", "valuetest").build();
      this.authorizationService.save(authorization);
      return new OAuth2AccessTokenAuthenticationToken(registeredClient, auth,
            accessToken, refreshToken, Collections.emptyMap());
   }

   private OAuth2RefreshToken generateRefreshToken(Duration tokenTimeToLive) {
      Instant issuedAt = Instant.now();
      Instant expiresAt = issuedAt.plus(tokenTimeToLive);
      return new OAuth2RefreshToken(this.refreshTokenGenerator.get(), issuedAt,
            expiresAt);
   }

   public static JwtClaimsSet.Builder accessTokenClaims(
         RegisteredClient registeredClient, String issuer, String subject,
         Set<String> authorizedScopes) {
      Instant issuedAt = Instant.now();
      Instant expiresAt = issuedAt.plus(
            registeredClient.getTokenSettings().getAccessTokenTimeToLive());
      JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder();
      if (StringUtils.hasText(issuer)) {
         claimsBuilder.issuer(issuer);
      }
      claimsBuilder.subject(subject)
            .audience(Collections.singletonList(registeredClient.getClientId()))
            .issuedAt(issuedAt).expiresAt(expiresAt).notBefore(issuedAt);
      if (!CollectionUtils.isEmpty(authorizedScopes)) {
         claimsBuilder.claim(OAuth2ParameterNames.SCOPE, authorizedScopes);
      }
      return claimsBuilder;
   }
}
