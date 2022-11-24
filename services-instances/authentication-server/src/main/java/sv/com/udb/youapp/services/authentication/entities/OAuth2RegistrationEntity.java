package sv.com.udb.youapp.services.authentication.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import sv.com.udb.youapp.services.authentication.enums.OAuth2Registration;

@Data
@Entity(name = "oauth2_registration_type")
public class OAuth2RegistrationEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int                id;
   @Enumerated(EnumType.STRING)
   @Column(name = "registration_type", nullable = false)
   private OAuth2Registration registrationType;
}
