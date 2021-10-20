package sv.com.udb.services.authentication.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import sv.com.udb.services.authentication.enums.IOAuthRegistrationType;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "oauth_registration_type")
@EqualsAndHashCode(exclude = { "registrations" })
@ToString(callSuper = true, exclude = { "registrations" })
@NamedEntityGraph(name = "oauth_registration",
                  attributeNodes = @NamedAttributeNode(value = "registrations"))
public class OAuthRegistrationType implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer                id;
   @Enumerated(EnumType.STRING)
   @Column(name = "registration_type", length = 64)
   private IOAuthRegistrationType name;
   @JsonBackReference
   @OneToMany(mappedBy = "registrationType")
   private Set<YouAppPrincipal>   registrations;
   private static final long      serialVersionUID = -1010572739049890222L;

   public static OAuthRegistrationType from(
         IOAuthRegistrationType registrationType) {
      return OAuthRegistrationType.builder()
            .id(registrationType.getPrimaryKey()).name(registrationType)
            .build();
   }
}
