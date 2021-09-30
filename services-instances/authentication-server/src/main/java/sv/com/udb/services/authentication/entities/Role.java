package sv.com.udb.services.authentication.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "role")
public class Role {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long                  id;
   @Column(length = 32, nullable = false)
   private String                name;
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "roles_privileges",
              joinColumns = @JoinColumn(name = "role_id",
                                        referencedColumnName = "id"),
              inverseJoinColumns = @JoinColumn(name = "privilege_id",
                                               referencedColumnName = "id"))
   private Collection<Privilege> privileges;
}