package sv.com.udb.services.authentication.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2TokenResponse {
   private String  access_token;
   private Instant expires_in;
   private String  id_token;
   private String  refresh_token;
   private String  scope;
   private String  token_type;
}
