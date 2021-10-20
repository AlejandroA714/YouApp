package sv.com.udb.services.authentication.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import sv.com.udb.services.authentication.enums.IPrivilege;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "privilege")
@ToString(exclude = "roles")
@EqualsAndHashCode(exclude = "roles")
@NamedEntityGraph(name = "privileges_role",
                  attributeNodes = @NamedAttributeNode(value = "roles"))
public class Privilege implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private int               id;
   @Enumerated(EnumType.STRING)
   @Column(length = 32, nullable = false)
   private IPrivilege        name;
   @JsonBackReference
   @ManyToMany(mappedBy = "privileges")
   private Set<Role>         roles;
   private static final long serialVersionUID = -8894977809912237473L;

   public static Privilege from(IPrivilege privilege) {
      return Privilege.builder().id(privilege.getPrimaryKey()).name(privilege)
            .build();
   }
}