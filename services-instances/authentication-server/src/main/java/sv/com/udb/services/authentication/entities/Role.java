package sv.com.udb.services.authentication.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import sv.com.udb.services.authentication.enums.IRole;

import javax.persistence.*;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "role")
@ToString(exclude = { "principals", "privileges" })
@EqualsAndHashCode(exclude = { "principals", "privileges" })
public class Role {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Integer                     id;
   @Enumerated(EnumType.STRING)
   @Column(length = 32, nullable = false)
   private IRole                       name;
   @JsonBackReference
   @ManyToMany(mappedBy = "roles")
   private Collection<YouAppPrincipal> principals;
   @Singular
   @ManyToMany
   @JsonManagedReference
   @JoinTable(name = "roles_privileges",
              joinColumns = @JoinColumn(name = "role_id",
                                        referencedColumnName = "id"),
              inverseJoinColumns = @JoinColumn(name = "privilege_id",
                                               referencedColumnName = "id"))
   private Collection<Privilege>       privileges;

   public static Role from(IRole role) {
      return Role.builder().id(role.getPrimaryKey()).name(role).build();
   }
}