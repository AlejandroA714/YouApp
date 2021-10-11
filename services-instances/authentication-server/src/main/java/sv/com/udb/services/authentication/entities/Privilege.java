package sv.com.udb.services.authentication.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import sv.com.udb.services.authentication.enums.IPrivilege;

import javax.persistence.*;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "privilege")
@ToString(exclude = "roles")
@EqualsAndHashCode(exclude = "roles")
public class Privilege {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private int              id;
   @Enumerated(EnumType.STRING)
   @Column(length = 32, nullable = false)
   private IPrivilege       name;
   @JsonBackReference
   @ManyToMany(mappedBy = "privileges")
   private Collection<Role> roles;
}