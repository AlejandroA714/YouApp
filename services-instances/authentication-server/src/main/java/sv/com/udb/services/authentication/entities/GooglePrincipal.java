package sv.com.udb.services.authentication.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sv.com.udb.services.authentication.enums.IOAuthRegistrationType;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GooglePrincipal implements Principal {
   @NotNull
   private String       id;
   @NotNull
   private String       email;
   @NotNull
   private String       familyName;
   @NotNull
   private String       givenName;
   @NotNull
   private String       name;
   private List<String> scopes;
   private String       photo;
   private LocalDate    birthday;

   @Override
   public String getNombres() {
      return this.givenName;
   }

   @Override
   public String getApellidos() {
      return this.familyName;
   }

   @Override
   public String getUsername() {
      return this.email;
   }

   @Override
   public boolean isActive() {
      return true;
   }

   @Override
   public IOAuthRegistrationType getOAuthRegistrationType() {
      return IOAuthRegistrationType.GOOGLE;
   }
}
