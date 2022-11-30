package sv.com.udb.youapp.services.authentication.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import sv.com.udb.youapp.services.authentication.enums.GrantTypeEnum;

@Data
@Entity(name = "client_grant_type")
public class GrantTypeEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int           id;
   @Enumerated(EnumType.STRING)
   @Column(nullable = false, unique = true)
   private GrantTypeEnum grantType;

   public String getGrantType() {
      return grantType.val();
   }
}
