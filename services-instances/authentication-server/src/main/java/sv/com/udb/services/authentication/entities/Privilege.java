package sv.com.udb.services.authentication.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sv.com.udb.services.authentication.enums.IPrivilege;

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
   private int        id;
   @Enumerated(EnumType.STRING)
   @Column(length = 32, nullable = false)
   private IPrivilege name;
}