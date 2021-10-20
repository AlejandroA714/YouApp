package sv.com.udb.services.authentication.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Valid
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "email_token")
@ToString(exclude = "user")
public class EmailToken implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer           id;
   @Column(nullable = false, length = 48)
   private String            token;
   @Column(name = "expiration_date", nullable = false)
   private LocalDateTime     expiration;
   @ManyToOne
   @JsonBackReference
   @JoinColumn(name = "user_id", referencedColumnName = "id")
   private YouAppPrincipal   user;
   private static final long serialVersionUID = -3616737586508310768L;
}
