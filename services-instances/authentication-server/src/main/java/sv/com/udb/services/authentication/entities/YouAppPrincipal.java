package sv.com.udb.services.authentication.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sv.com.udb.services.authentication.converter.DateConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
@ToString(callSuper = true)
public class YouAppPrincipal implements UserDetails {
   @Id
   private String                Id;
   @Column(name = "given_name", length = 32, nullable = false)
   private String                nombres;
   @Column(name = "family_name", length = 32, nullable = false)
   private String                apellidos;
   @Column(unique = true, length = 48, nullable = false)
   private String                email;
   @Column(unique = true, length = 32, nullable = false)
   private String                username;
   @Column(length = 512)
   private String                password;
   @Column
   @Convert(converter = DateConverter.class)
   private LocalDate             birthday;
   @Column(length = 256)
   private String                description;
   @Column(length = 512)
   private String                photo;
   @Column(name = "last_login")
   private LocalDateTime         lastLogin;
   @Column(name = "email_confirmed")
   private boolean               isActive;
   @ManyToOne
   private OAuthRegistrationType registrationType;
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "users_roles",
              joinColumns = @JoinColumn(name = "user_id",
                                        referencedColumnName = "id"),
              inverseJoinColumns = @JoinColumn(name = "role_id",
                                               referencedColumnName = "id"))
   private Collection<Role>      roles;

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      List<String> authorities = new ArrayList<>();
      roles.forEach(r -> {
         authorities.add(r.getName().toString());
         authorities.addAll(r.getPrivileges().stream()
               .map(p -> p.getName().toString()).collect(Collectors.toSet()));
      });
      var x = authorities.stream().map(s -> new SimpleGrantedAuthority(s))
            .collect(Collectors.toList());
      return x;
   }

   @Override
   public boolean isAccountNonExpired() {
      return this.isActive;
   }

   @Override
   public boolean isAccountNonLocked() {
      return this.isActive;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return this.isActive;
   }

   @Override
   public boolean isEnabled() {
      return this.isActive;
   }
}
