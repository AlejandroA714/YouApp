package sv.com.udb.youapp.services.authentication.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@Entity(name = "user")
@ToString(of = {"id", "username", "email"})
@EqualsAndHashCode(of = {"id", "username", "email"})
public class UserEntity {
  @Id
  private String id;
  @Column(name = "given_name", length = 32, nullable = false)
  private String nombres;
  @Column(name = "family_name", length = 32, nullable = false)
  private String apellidos;
  @Column(length = 48, nullable = false)
  private String email;
  @Column(length = 32, nullable = false)
  private String username;
  @Column(length = 512, nullable = false)
  private String password;
  @Column
  @PastOrPresent
  private LocalDate birthday;
  @Column
  private String photo;
  @Column(name = "registration_date")
  private LocalDate registrationDate;
  @Column(name = "email_confirmed")
  private boolean isActive;
  @ManyToOne
  @JoinColumn(nullable = false)
  private OAuth2RegistrationEntity registrationType;
  @ManyToOne
  @JoinColumn
  private RoleEntity role;
}
