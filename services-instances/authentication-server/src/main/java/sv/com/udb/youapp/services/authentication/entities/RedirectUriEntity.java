package sv.com.udb.youapp.services.authentication.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "client_redirect_uri")
public class RedirectUriEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int    id;
   @Column(nullable = false, unique = true)
   private String uri;
}
