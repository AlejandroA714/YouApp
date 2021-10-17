package sv.com.udb.services.authentication.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import sv.com.udb.services.authentication.enums.IOAuthRegistrationType;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "oauth_registration_type")
@EqualsAndHashCode(exclude = { "registrations" })
@ToString(callSuper = true, exclude = { "registrations" })
public class OAuthRegistrationType {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer                     id;
   @Enumerated(EnumType.STRING)
   @Column(name = "registration_type", length = 64)
   private IOAuthRegistrationType      name;
   @JsonBackReference
   @OneToMany(mappedBy = "registrationType")
   private Collection<YouAppPrincipal> registrations;

   public static OAuthRegistrationType from(
         IOAuthRegistrationType registrationType) {
      return OAuthRegistrationType.builder()
            .id(registrationType.getPrimaryKey()).name(registrationType)
            .build();
   }
}
