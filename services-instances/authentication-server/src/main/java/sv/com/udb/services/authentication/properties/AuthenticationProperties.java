package sv.com.udb.services.authentication.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@Valid
@NoArgsConstructor
public class AuthenticationProperties {

  @NotNull
  private AuthKeys keys;
  @NotNull
  private JWTConfiguration jwt;
  @NotNull
  private ClientConfiguration client;

  @Data
  @Valid
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ClientConfiguration{
    @NotNull
    private String ClientId;
    @NotNull
    private String ClienSecret;
    private ClientAuthenticationMethod AuthenticationMethod = ClientAuthenticationMethod.CLIENT_SECRET_POST;
    private AuthorizationGrantType GrantType = AuthorizationGrantType.AUTHORIZATION_CODE;
  }


  @Data
  @Valid
  @NoArgsConstructor
  @AllArgsConstructor
  public static class AuthKeys{
    @NotNull
    private String privateKey;
    @NotNull
    private String publicKey;
  }

  @Data
  @Valid
  @NoArgsConstructor
  public static class JWTConfiguration{
    @NotNull
    private String key;
    @NotNull
    private long duration;
  }


}
