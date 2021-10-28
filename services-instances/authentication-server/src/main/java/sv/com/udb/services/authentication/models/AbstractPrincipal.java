package sv.com.udb.services.authentication.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@Data
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@ToString(callSuper = true)
public class AbstractPrincipal implements Principal {
   @NotNull
   @Size(min = 5, max = 32, message = "Nombre no valido")
   @Column(name = "given_name", length = 32, nullable = false)
   protected String                               nombres;
   @NotNull
   @Size(min = 5, max = 32, message = "Apellido no valido")
   @Column(name = "family_name", length = 32, nullable = false)
   protected String                               apellidos;
   @NotNull
   @Email(message = "Correo Invalido")
   @Size(min = 5, max = 48, message = "Correo Invalido")
   @Column(unique = true, length = 48, nullable = false)
   protected String                               email;
   @NotNull
   @Size(min = 5, max = 32, message = "Nombre de usuario invalido")
   @Column(unique = true, length = 32, nullable = false)
   protected String                               username;
   @Column(length = 512)
   @Size(min = 4, max = 512, message = "Contraseña invalida")
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   protected String                               password;
   protected LocalDate                            birthday;
   @Column(length = 512)
   protected String                               photo;
   @Transient
   private String                                 id = UUID.randomUUID()
         .toString();
   @Transient
   private Collection<? extends GrantedAuthority> authorities;
   @Transient
   private boolean                                active;

   @Override
   public String getId() {
      return this.id;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return authorities;
   }

   @Override
   public boolean isEnabled() {
      return this.active;
   }

   @Override
   public String getFullName() {
      return String.format("%s %s", nombres, apellidos);
   }

   public static AbstractPrincipal from(YouAppPrincipal p) {
      return AbstractPrincipal.builder().id(p.getId()).nombres(p.getNombres())
            .apellidos(p.getApellidos()).email(p.getEmail())
            .username(p.getUsername()).password(p.getPassword())
            .birthday(p.getBirthday()).active(p.isEnabled())
            .authorities(p.getAuthorities()).photo(p.getPhoto()).build();
   }
}
