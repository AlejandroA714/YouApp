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

import java.util.Map;

import static sv.com.udb.youapp.services.authentication.utils.MetadataUtils.TOKEN_INVALIDATED;
import static sv.com.udb.youapp.services.authentication.utils.MetadataUtils.getSettings;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "authorization_code_metadata")
public class AuthorizationCodeMetadataEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int     id;
   @Column(name = "ena")
   private boolean invalidated;

   public Map<String, Object> toPOJO() {
      return Map.of(TOKEN_INVALIDATED, invalidated);
   }

   public static AuthorizationCodeMetadataEntity create(
         Map<String, Object> map) {
      return AuthorizationCodeMetadataEntity.builder()
            .invalidated(getSettings(map, TOKEN_INVALIDATED)).build();
   }
}
