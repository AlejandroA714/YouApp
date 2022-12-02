package sv.com.udb.youapp.services.authentication.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.time.Instant;
import java.util.Map;

import static org.springframework.security.oauth2.server.authorization.OAuth2Authorization.Token.CLAIMS_METADATA_NAME;
import static sv.com.udb.youapp.services.authentication.utils.MetadataUtils.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "access_token_metadata")
public class AccessTokenMetadataEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int     id;
   @Column
   private String  sub;
   @Column
   private String  aud;
   @Column
   private Instant nbf;
   @Column
   private String  scopes;
   @Column
   private URL     iss;
   @Column
   private Instant exp;
   @Column
   private Instant iat;
   @Column
   private boolean ena;

   public Map<String, Object> toPOJO() {
      return Map.of("asd", "");
   }

   public static AccessTokenMetadataEntity create(Map<String, Object> payload) {
      var claims = (Map<String, Object>) payload.get(CLAIMS_METADATA_NAME);
      return AccessTokenMetadataEntity.builder().sub(getSettings(claims, SUB))
            .ena(getSettings(payload, TOKEN_INVALIDATED))
            .nbf(getSettings(claims, NBF)).iss(getSettings(claims, ISS))
            .exp(getSettings(claims, EXP)).iat(getSettings(claims, IAT))
            .aud(StringUtils
                  .collectionToCommaDelimitedString(getSettings(claims, AUD)))
            .scopes(StringUtils.collectionToCommaDelimitedString(
                  getCollectionSettings(claims, SCOPE)))
            .build();
   }
}
