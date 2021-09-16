package sv.com.udb.services.authentication.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@Valid
@NoArgsConstructor
public class AuthenticationProperties {

  private AuthKeys keys;

  private JWTConfiguration jwt;

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
