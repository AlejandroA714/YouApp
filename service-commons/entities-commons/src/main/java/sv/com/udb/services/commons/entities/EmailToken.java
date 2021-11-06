package sv.com.udb.services.commons.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Valid
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")
@Entity(name = "email_token")
@EqualsAndHashCode(exclude = "user")
public class EmailToken implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer           id;
   @Column(nullable = false, length = 48)
   private String            token;
   @Column(name = "expiration_date", nullable = false)
   private LocalDateTime     expiration;
   @ManyToOne
   @JsonManagedReference
   @JoinColumn(name = "user_id", referencedColumnName = "id")
   private YouAppPrincipal   user;
   private static final long serialVersionUID = -3616737586508310768L;
}
