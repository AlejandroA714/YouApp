package sv.com.udb.youapp.services.authentication.entities;

import lombok.Data;
import sv.com.udb.youapp.services.authentication.enums.OAuth2Registration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "oauth2_registration_type")
public class OAuth2RegistrationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Enumerated(EnumType.STRING)
  @Column(name = "registration_type",nullable = false)
  private OAuth2Registration registrationType;

}
