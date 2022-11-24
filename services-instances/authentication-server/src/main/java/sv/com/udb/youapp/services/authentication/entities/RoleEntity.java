package sv.com.udb.youapp.services.authentication.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sv.com.udb.youapp.services.authentication.enums.OAuth2Role;
import java.util.Set;

@Data
@Entity(name = "role")
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
@NamedEntityGraph(name = "rolesWithUsers",
                  attributeNodes = { @NamedAttributeNode("user") })
public class RoleEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int             id;
   @Enumerated(EnumType.STRING)
   @Column(name = "name", nullable = false)
   private OAuth2Role      role;
   @OneToMany(mappedBy = "role")
   private Set<UserEntity> user;
}
