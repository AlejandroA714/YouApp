package sv.com.udb.services.authentication.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import sv.com.udb.services.authentication.properties.AuthenticationProperties;
import sv.com.udb.services.authentication.repository.PrincipalRepository;
import sv.com.udb.services.authentication.services.AuthService;
import sv.com.udb.services.authentication.services.DefaultAuthService;
import sv.com.udb.services.authentication.services.DefaultEncryptionPasswordService;
import sv.com.udb.services.authentication.services.EncryptionPasswordService;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
public class AuthenticationServerConfiguration {
    private static final String RSA      = "RSA";
    private static final int    KEY_SIZE = 2048;

    @Bean
    // @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http.formLogin(Customizer.withDefaults()).csrf().disable()
                .build();
    }

    @Bean
    public RoleHierarchy roleHierarchy(
            AuthenticationProperties authProperties) {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(authProperties.getRoleHierarchy());
        return roleHierarchy;
    }
    // @Bean
    // public DefaultWebSecurityExpressionHandler
    // webSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
    // DefaultWebSecurityExpressionHandler expressionHandler = new
    // DefaultWebSecurityExpressionHandler();
    // expressionHandler.setRoleHierarchy(roleHierarchy);
    // return expressionHandler;
    // }

    @Bean
    @ConfigurationProperties("auth")
    public AuthenticationProperties authProperties() {
        return new AuthenticationProperties();
    }

    @Bean
    public AuthService authService(PrincipalRepository userRepository) {
        return new DefaultAuthService(userRepository);
    }

    @Bean
    public EncryptionPasswordService encryptionPasswordService(
            AuthenticationProperties authProperties) {
        return new DefaultEncryptionPasswordService(authProperties);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource()
            throws NoSuchAlgorithmException {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public TokenSettings tokenSettings(
            AuthenticationProperties authProperties) {
        return TokenSettings.builder()
                .accessTokenTimeToLive(
                        authProperties.getJwt().getAccess_token())
                .refreshTokenTimeToLive(
                        authProperties.getJwt().getRefresh_token())
                .build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(
            AuthenticationProperties authProperties,
            TokenSettings tokenSettings) {
        RegisteredClient registeredClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId(authProperties.getClient().getClientId())
                .clientSecret(authProperties.getClient().getClientSecret())
                .clientAuthenticationMethods(ca -> ca.addAll(
                        authProperties.getClient().getAuthenticationMethods()))
                .authorizationGrantTypes(gt -> gt
                        .addAll(authProperties.getClient().getGrantTypes()))
                .redirectUris(
                        uris -> uris.addAll(authProperties.getRedirectUris()))
                .scope(OidcScopes.OPENID).tokenSettings(tokenSettings).build();
        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder().issuer("http://auth-server:8083")
                .build();
    }

    private static RSAKey generateRsa() throws NoSuchAlgorithmException {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey).privateKey(privateKey)
                .keyID(UUID.randomUUID().toString()).build();
    }

    private static KeyPair generateRsaKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }
}
