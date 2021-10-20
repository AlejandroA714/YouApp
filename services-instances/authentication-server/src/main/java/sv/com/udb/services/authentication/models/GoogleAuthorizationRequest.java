package sv.com.udb.services.authentication.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sv.com.udb.services.authentication.enums.IPrivilege;
import sv.com.udb.services.authentication.enums.IRole;
import sv.com.udb.services.authentication.models.GooglePrincipal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
public class GoogleAuthorizationRequest implements Authentication {
   @NotNull
   private String            idToken;
   @NotNull
   private String            accessToken;
   @Valid
   @NotNull
   private GooglePrincipal   principal;
   @JsonProperty(access = JsonProperty.Access.READ_ONLY)
   private boolean           authenticated;
   private String            serverAuthCode;
   private static final long serialVersionUID = 8172113494027008843L;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return principal.getAuthorities();
   }

   @Override
   public Object getCredentials() {
      return getIdToken();
   }

   @Override
   public Object getDetails() {
      return this.principal;
   }

   @Override
   public boolean isAuthenticated() {
      return this.authenticated;
   }

   @Override
   public void setAuthenticated(boolean isAuthenticated)
         throws IllegalArgumentException {
      this.authenticated = isAuthenticated;
   }

   @Override
   public String getName() {
      return this.principal.getName();
   }
}
