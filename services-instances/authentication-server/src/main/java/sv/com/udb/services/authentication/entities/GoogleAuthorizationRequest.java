package sv.com.udb.services.authentication.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;
import sv.com.udb.services.authentication.enums.IPrivilege;
import sv.com.udb.services.authentication.enums.IRole;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@With
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAuthorizationRequest implements Authentication {
   @NotNull
   private String          idToken;
   @NotNull
   private String          accessToken;
   @NotNull
   private List<String>    scopes;
   @Valid
   @NotNull
   private GooglePrincipal principal;
   @JsonProperty(access = JsonProperty.Access.READ_ONLY)
   private boolean         authenticated;
   private String          serverAuthCode;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return Arrays.asList(
            new SimpleGrantedAuthority(IRole.ROLE_USER.toString()),
            new SimpleGrantedAuthority(IPrivilege.READ_PRIVILEGE.toString()));
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
