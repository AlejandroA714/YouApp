package sv.com.udb.services.authentication.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Validated
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
@ToString(callSuper = true, exclude = { "roles", "registrationType" })
@EqualsAndHashCode(callSuper = true, exclude = { "roles", "registrationType" })
public class YouAppPrincipal extends AbstractPrincipal implements UserDetails {
   @Id
   @GeneratedValue(generator = "uuid2")
   @GenericGenerator(name = "uuid2",
                     strategy = "org.hibernate.id.UUIDGenerator")
   private String                Id;
   @Column(name = "last_login")
   private LocalDateTime         lastLogin;
   @Column(name = "email_confirmed")
   private Boolean               isActive;
   @ManyToOne
   @JsonManagedReference
   @MapKeyColumn(name = "name")
   private OAuthRegistrationType registrationType;
   @Singular
   @ManyToMany
   @JsonManagedReference
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

   @Override
   public String getId() {
      return this.Id;
   }

   @Override
   public boolean isActive() {
      return this.isActive;
   }

   public static YouAppPrincipal from(Principal principal) {
      return YouAppPrincipal.builder().Id(principal.getId())
            .email(principal.getEmail()).nombres(principal.getNombres())
            .apellidos(principal.getApellidos())
            .username(principal.getUsername()).photo(principal.getPhoto())
            .isActive(principal.isActive())
            .registrationType(OAuthRegistrationType
                  .from(principal.getOAuthRegistrationType()))
            .role(Role.from(principal.getRol()))
            .birthday(principal.getBirthday()).build();
   }

   @JsonIgnore
   public Map<String, Object> getFields() {
      Map<String, Object> cFields = new HashMap<>();
      List<java.lang.reflect.Field> fields = Stream
            .concat(Stream.of(getClass().getDeclaredFields()),
                  Stream.of(getClass().getSuperclass().getDeclaredFields()))
            .filter(x -> x.getType().equals(String.class)
                  || x.getType().equals(LocalDate.class))
            .collect(Collectors.toList());
      fields.forEach(f -> {
         try {
            Object p = new PropertyDescriptor(f.getName(),
                  YouAppPrincipal.class).getReadMethod().invoke(this);
            cFields.put(f.getName(), p);
         }
         catch (IllegalAccessException | IntrospectionException
               | InvocationTargetException e) {
            cFields.put(f.getName(), null);
            e.printStackTrace();
         }
      });
      return cFields;
   }
}
