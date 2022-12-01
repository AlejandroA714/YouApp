package sv.com.udb.youapp.services.authentication.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "registered_client")
public class RegisteredClientEntity {
   @Id
   private String                          id;
   @Column(nullable = false)
   private String                          clientId;
   @Column(nullable = false)
   private Instant                         clientIssuedAt;
   @Column
   private String                          clientSecret;
   @Column
   private Instant                         clientSecretExpiresAt;
   @Column
   private String                          clientName;
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "registered_client_authentication_method")
   private Set<AuthenticationMethodEntity> authenticationMethods;
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "registered_client_grant_type")
   private Set<GrantTypeEntity>            grantTypes;
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "registered_client_redirect_uri")
   private Set<RedirectUriEntity>          redirectUris;
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "registered_client_scope")
   private Set<ScopeEntity>                scopes;

   public void clientMethods(Set<ClientAuthenticationMethod> clientMethods) {
      clientMethods.addAll(authenticationMethods.stream()
            .map(AuthenticationMethodEntity::getMethod)
            .map(ClientAuthenticationMethod::new).toList());
   }

   public void grantTypes(Set<AuthorizationGrantType> grants) {
      grants.addAll(grantTypes.stream().map(GrantTypeEntity::getGrantType)
            .map(AuthorizationGrantType::new).toList());
   }

   public void redirectUris(Set<String> uris) {
      uris.addAll(
            redirectUris.stream().map(RedirectUriEntity::getUri).toList());
   }

   public void scopes(Set<String> s) {
      s.addAll(scopes.stream().map(ScopeEntity::getScope).toList());
   }
}
