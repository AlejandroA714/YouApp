package sv.com.udb.services.commons.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import sv.com.udb.services.commons.enums.IRole;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "role")
@ToString(exclude = { "privileges", "principals" })
@EqualsAndHashCode(exclude = { "privileges", "principals" })
@NamedEntityGraphs({
      @NamedEntityGraph(name = "roles_privileges",
                        attributeNodes = @NamedAttributeNode(value = "privileges")),
      @NamedEntityGraph(name = "roles_principals",
                        attributeNodes = {
                              @NamedAttributeNode(value = "principals"),
                              @NamedAttributeNode(value = "privileges") }) })
public class Role implements Serializable {
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
   private Set<Privilege>              privileges;
   private static final long           serialVersionUID = -1076892611352691032L;

   public static Role from(IRole role) {
      return Role.builder().id(role.getPrimaryKey()).name(role).build();
   }
}
