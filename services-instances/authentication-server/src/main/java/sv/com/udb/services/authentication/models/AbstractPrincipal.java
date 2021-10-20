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
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;

@Data
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@ToString(callSuper = true)
public class AbstractPrincipal implements Principal {
   @NotNull
   @Column(name = "given_name", length = 32, nullable = false)
   protected String                               nombres;
   @NotNull
   @Column(name = "family_name", length = 32, nullable = false)
   protected String                               apellidos;
   @NotNull
   @Column(unique = true, length = 48, nullable = false)
   protected String                               email;
   @NotNull
   @Column(unique = true, length = 32, nullable = false)
   protected String                               username;
   @Column(length = 512)
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   protected String                               password;
   protected LocalDate                            birthday;
   @Column(length = 512)
   protected String                               photo;
   @Transient
   // @JsonProperty(access = JsonProperty.Access.READ_ONLY)
   private String                                 id;
   @Transient
   // @JsonProperty(access = JsonProperty.Access.READ_ONLY)
   private Collection<? extends GrantedAuthority> authorities;
   @Transient
   // @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
      return String.format("%S %S", nombres, apellidos);
   }

   public static AbstractPrincipal from(YouAppPrincipal p) {
      return AbstractPrincipal.builder().id(p.getId()).nombres(p.getNombres())
            .apellidos(p.getApellidos()).email(p.getEmail())
            .username(p.getUsername()).password(p.getPassword())
            .birthday(p.getBirthday()).active(p.isEnabled())
            .authorities(p.getAuthorities()).photo(p.getPhoto()).build();
   }
}
