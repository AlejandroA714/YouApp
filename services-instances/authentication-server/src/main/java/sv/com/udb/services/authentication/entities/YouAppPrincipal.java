package sv.com.udb.services.authentication.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;
import sv.com.udb.services.authentication.enums.IPrivilege;
import sv.com.udb.services.authentication.models.AbstractPrincipal;
import sv.com.udb.services.authentication.models.Principal;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Validated
@SuperBuilder
@NoArgsConstructor
@Entity(name = "user")
@ToString(callSuper = true, exclude = { "roles", "emailTokens" })
@EqualsAndHashCode(callSuper = true, exclude = { "roles", "emailTokens" })
@NamedEntityGraph(name = "user_roles",
                  attributeNodes = @NamedAttributeNode(value = "roles",
                                                       subgraph = "user_role_privileges"),
                  subgraphs = @NamedSubgraph(name = "user_role_privileges",
                                             attributeNodes = @NamedAttributeNode(value = "privileges")))
public class YouAppPrincipal extends AbstractPrincipal {
   private static final long      serialVersionUID = 7389128175350348769L;
   @Id
   private String                 id;
   @Column(name = "registration_date")
   private LocalDate              registration;
   @Column(name = "email_confirmed")
   private Boolean                isActive;
   @ManyToOne
   @JsonManagedReference
   private OAuthRegistrationType  registrationType;
   @JsonManagedReference
   @OneToMany(mappedBy = "user")
   private Collection<EmailToken> emailTokens;
   @Singular
   @ManyToMany
   @JsonManagedReference
   @JoinTable(name = "users_roles",
              joinColumns = @JoinColumn(name = "user_id",
                                        referencedColumnName = "id"),
              inverseJoinColumns = @JoinColumn(name = "role_id",
                                               referencedColumnName = "id"))
   private Set<Role>              roles;

   @PrePersist
   public void initializeUUID() {
      if (id == null) {
         id = UUID.randomUUID().toString();
      }
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
   @JsonIgnore
   public Collection<? extends GrantedAuthority> getAuthorities() {
      List<String> authorities = new ArrayList<>();
      roles.forEach(r -> {
         authorities.add(r.getName().toString());
         authorities.addAll(r.getPrivileges().stream()
               .map(p -> p.getName().toString()).collect(Collectors.toSet()));
      });
      return authorities.stream().map(s -> new SimpleGrantedAuthority(s))
            .collect(Collectors.toList());
   }

   public static YouAppPrincipal from(Principal principal) {
      return YouAppPrincipal.builder().id(principal.getId())
            .email(principal.getEmail()).nombres(principal.getNombres())
            .apellidos(principal.getApellidos())
            .username(principal.getUsername()).photo(principal.getPhoto())
            .isActive(principal.isEnabled())
            .registrationType(OAuthRegistrationType
                  .from(principal.getOAuthRegistrationType()))
            .registration(principal.getRegistration())
            .role(Role.from(principal.getRol()))
            .birthday(principal.getBirthday()).build();
   }
}
