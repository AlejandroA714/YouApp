package sv.com.udb.services.authentication.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Data
@Valid
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "email_token")
public class EmailToken {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer         id;
   @Column(nullable = false, length = 48)
   private String          token;
   @Column(name = "expiration_date", nullable = false)
   private LocalDateTime   expiration;
   @ManyToOne
   @JsonManagedReference
   @JoinColumn(name = "user_id", referencedColumnName = "id")
   private YouAppPrincipal user;
}
