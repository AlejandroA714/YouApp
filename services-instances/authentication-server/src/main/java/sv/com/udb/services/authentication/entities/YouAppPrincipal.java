package sv.com.udb.services.authentication.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@ToString(callSuper = true,
          exclude = { "roles", "registrationType", "emailTokens" })
@EqualsAndHashCode(callSuper = true,
                   exclude = { "roles", "registrationType", "emailTokens" })
public class YouAppPrincipal extends AbstractPrincipal implements UserDetails {
   @Id
   private String                id;
   @Column(name = "registration_date")
   private LocalDate             registration;
   @Column(name = "email_confirmed")
   private Boolean               isActive;
   @ManyToOne
   @JsonManagedReference
   private OAuthRegistrationType registrationType;
   @JsonIgnore
   @JsonManagedReference
   @OneToMany(mappedBy = "user")
   private List<EmailToken>      emailTokens;
   @Singular
   @JsonManagedReference
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

   @Override
   public String getId() {
      return this.id;
   }

   @Override
   public boolean isActive() {
      return this.isActive;
   }

   @PrePersist
   public void initializeUUID() {
      if (id == null) {
         id = UUID.randomUUID().toString();
      }
   }

   public static YouAppPrincipal from(Principal principal) {
      return YouAppPrincipal.builder().id(principal.getId())
            .email(principal.getEmail()).nombres(principal.getNombres())
            .apellidos(principal.getApellidos())
            .username(principal.getUsername()).photo(principal.getPhoto())
            .isActive(principal.isActive())
            .registrationType(OAuthRegistrationType
                  .from(principal.getOAuthRegistrationType()))
            .registration(principal.getRegistration())
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
