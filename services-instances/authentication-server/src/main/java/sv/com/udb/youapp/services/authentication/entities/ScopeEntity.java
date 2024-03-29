package sv.com.udb.youapp.services.authentication.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "client_scope")
public class ScopeEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int    id;
   @Column(nullable = false, unique = true)
   private String scope;
}
