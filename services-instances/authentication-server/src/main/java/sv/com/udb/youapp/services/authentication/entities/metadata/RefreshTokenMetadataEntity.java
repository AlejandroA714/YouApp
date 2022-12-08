package sv.com.udb.youapp.services.authentication.entities.metadata;

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
public class RefreshTokenMetadataEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int     id;
   @Column(name = "ena")
   private boolean invalidated;

   public Map<String, Object> toPOJO() {
      return Map.of(TOKEN_INVALIDATED, invalidated);
   }

   public static RefreshTokenMetadataEntity create(
         Map<String, Object> map) {
      return RefreshTokenMetadataEntity.builder()
            .invalidated(getSettings(map, TOKEN_INVALIDATED)).build();
   }
}
