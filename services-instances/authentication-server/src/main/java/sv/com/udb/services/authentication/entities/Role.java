package sv.com.udb.services.authentication.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sv.com.udb.services.authentication.enums.IRole;

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
   private Integer               id;
   @Enumerated(EnumType.STRING)
   @Column(length = 32, nullable = false)
   private IRole                 name;
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "roles_privileges",
              joinColumns = @JoinColumn(name = "role_id",
                                        referencedColumnName = "id"),
              inverseJoinColumns = @JoinColumn(name = "privilege_id",
                                               referencedColumnName = "id"))
   private Collection<Privilege> privileges;
}