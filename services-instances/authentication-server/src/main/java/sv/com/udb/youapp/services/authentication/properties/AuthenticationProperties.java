package sv.com.udb.youapp.services.authentication.properties;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@NoArgsConstructor
public class AuthenticationProperties {
   @NotNull
   private AuthKeys keys;

   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public static class AuthKeys {
      @NotNull
      private String privateKey;
      @NotNull
      private String publicKey;
   }
}
