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
@Entity(name = "privilege")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long   id;
    @Column(length = 32, nullable = false)
    private String name;
    // @ManyToMany
    // @JoinTable(name = "roles_privileges",
    // inverseJoinColumns = @JoinColumn( name = "role_id", referencedColumnName
    // = "id"),
    // joinColumns = @JoinColumn( name = "privilege_id", referencedColumnName =
    // "id"))
    // private Collection<Role> roles;
}