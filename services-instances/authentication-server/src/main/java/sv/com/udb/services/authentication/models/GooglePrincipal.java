package sv.com.udb.services.authentication.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sv.com.udb.services.authentication.enums.IOAuthRegistrationType;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GooglePrincipal implements Principal {
   @NotNull
   private String                                 id;
   @NotNull
   private String                                 email;
   @NotNull
   private String                                 givenName;
   @NotNull
   private String                                 familyName;
   @NotNull
   private String                                 name;
   private List<String>                           scopes;
   private String                                 photo;
   private LocalDate                              birthday;
   @JsonProperty(access = JsonProperty.Access.READ_ONLY)
   private Collection<? extends GrantedAuthority> authorities;
   private static final long                      serialVersionUID = 6837376200581689090L;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return authorities;
   }

   @Override
   public String getNombres() {
      return this.givenName;
   }

   @Override
   public String getApellidos() {
      return this.familyName;
   }

   @Override
   public String getFullName() {
      return name;
   }

   @Override
   public String getPassword() {
      return null;
   }

   @Override
   public String getUsername() {
      return this.email;
   }

   @Override
   public IOAuthRegistrationType getOAuthRegistrationType() {
      return IOAuthRegistrationType.GOOGLE;
   }
}
