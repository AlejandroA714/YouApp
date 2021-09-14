package sv.com.udb.services.authentication.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Valid
@NoArgsConstructor
public class AuthenticationProperties {

  private AuthKeys keys;

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

}
