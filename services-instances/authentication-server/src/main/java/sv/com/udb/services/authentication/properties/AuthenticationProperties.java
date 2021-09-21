package sv.com.udb.services.authentication.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;

@Data
@Valid
@NoArgsConstructor
public class AuthenticationProperties {
    @NotNull
    private AuthKeys            keys;
    @NotNull
    private JWTConfiguration    jwt;
    @NotNull
    private ClientConfiguration client;
    @NotNull
    private List<String>        redirectUris;

    @Data
    @Valid
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientConfiguration {
        @NotNull
        private String                           ClientId;
        @NotNull
        private String                           ClientSecret;
        @NotNull
        private List<ClientAuthenticationMethod> AuthenticationMethods;
        @NotNull
        private List<AuthorizationGrantType>     GrantTypes;
    }

    @Data
    @Valid
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthKeys {
        @NotNull
        private String privateKey;
        @NotNull
        private String publicKey;
    }

    @Data
    @Valid
    @NoArgsConstructor
    public static class JWTConfiguration {
        @NotNull
        private Duration access_token;
        @NotNull
        private Duration refresh_token;
    }
}
