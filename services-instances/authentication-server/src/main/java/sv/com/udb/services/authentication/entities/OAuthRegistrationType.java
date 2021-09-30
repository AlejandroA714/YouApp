package sv.com.udb.services.authentication.entities;

import lombok.*;

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
   @Column(name = "registration_type", length = 64)
   private String                      name;
   @OneToMany(targetEntity = YouAppPrincipal.class)
   private Collection<YouAppPrincipal> registrations;
}
