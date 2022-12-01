package sv.com.udb.youapp.commons.jpa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sv.com.udb.youapp.commons.jpa.entities.PrincipalEntity;
import sv.com.udb.youapp.commons.jpa.enums.OAuth2Registration;
import sv.com.udb.youapp.commons.jpa.enums.OAuth2Role;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
   private String             id;
   @NotBlank
   @Size(max = 32)
   private String             nombres;
   @NotBlank
   @Size(max = 32)
   private String             apellidos;
   @NotBlank
   @Size(max = 32)
   private String             username;
   @Email
   @NotBlank
   @Size(max = 48)
   private String             email;
   @Setter(AccessLevel.PRIVATE)
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   private String             password;
   @NotNull
   @PastOrPresent
   private LocalDate          birthday;
   private LocalDate          registrationDate;
   private String             photo;
   private boolean            isActive;
   private OAuth2Registration registrationType;
   private OAuth2Role         role;

   public static User map(PrincipalEntity e) {
      return User.builder().id(e.getId()).nombres(e.getNombres())
            .apellidos(e.getApellidos()).username(e.getUsername())
            .password(e.getPassword()).email(e.getEmail())
            .birthday(e.getBirthday()).registrationDate(e.getRegistrationDate())
            .isActive(e.isActive())
            .registrationType(e.getRegistrationType().getRegistrationType())
            .role(e.getRole().getRole()).build();
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return Arrays.asList(new SimpleGrantedAuthority(role.name()));
   }

   public void setAuthorities(
         Collection<? extends GrantedAuthority> authorities) {
      this.role = authorities.stream().findFirst()
            .map(GrantedAuthority::getAuthority).map(OAuth2Role::valueOf)
            .orElse(OAuth2Role.ROLE_USER);
   }

   @Override
   public boolean isAccountNonExpired() {
      return isActive;
   }

   @Override
   public boolean isAccountNonLocked() {
      return isActive;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return isActive;
   }

   @Override
   public boolean isEnabled() {
      return isActive;
   }
}
