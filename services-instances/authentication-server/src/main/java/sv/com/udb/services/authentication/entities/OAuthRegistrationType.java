package sv.com.udb.services.authentication.entities;

import lombok.*;
import sv.com.udb.services.authentication.enums.IOAuthRegistrationType;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "oauth_registration_type")
public class OAuthRegistrationType {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer                     id;
   @Enumerated(EnumType.STRING)
   @Column(name = "registration_type", length = 64)
   private IOAuthRegistrationType      name;
   @OneToMany(targetEntity = YouAppPrincipal.class)
   private Collection<YouAppPrincipal> registrations;
}
