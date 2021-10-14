package sv.com.udb.services.authentication.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true)
public class AbstractPrincipal implements Principal {
   @NotNull
   @Column(name = "given_name", length = 32, nullable = false)
   protected String    nombres;
   @NotNull
   @Column(name = "family_name", length = 32, nullable = false)
   protected String    apellidos;
   @NotNull
   @Column(unique = true, length = 48, nullable = false)
   protected String    email;
   @NotNull
   @Column(unique = true, length = 32, nullable = false)
   protected String    username;
   @Column(length = 512)
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   protected String    password;
   protected LocalDate birthday;
   @Column(length = 512)
   protected String    photo;

   @Override
   public String getId() {
      return null;
   }

   @Override
   public boolean isActive() {
      return false;
   }
}
