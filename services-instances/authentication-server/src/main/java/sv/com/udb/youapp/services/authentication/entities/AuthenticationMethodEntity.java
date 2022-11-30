package sv.com.udb.youapp.services.authentication.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sv.com.udb.youapp.services.authentication.enums.AuthenticationMethodEnum;

@Data
@EqualsAndHashCode
@Entity(name = "client_authentication_method")
public class AuthenticationMethodEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int                      id;
   @Enumerated(EnumType.STRING)
   @Column(nullable = false, unique = true)
   private AuthenticationMethodEnum method;
   public String getMethod() {
      return method.val();
   }
}
