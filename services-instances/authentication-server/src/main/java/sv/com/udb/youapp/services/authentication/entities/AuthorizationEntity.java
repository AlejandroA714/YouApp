package sv.com.udb.youapp.services.authentication.entities;

import java.time.Instant;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "client_authorization")
public class AuthorizationEntity {
   @Id
   @Column
   private String                          id;
   private String                          registeredClientId;
   private String                          principalName;
   private String                          authorizationGrantType;
   private String                          authorizedScopes;
   @Column(length = 4000)
   private String                          attributes;
   @Column(length = 500)
   private String                          state;
   @Column(length = 4000)
   private String                          authorizationCodeValue;
   private Instant                         authorizationCodeIssuedAt;
   private Instant                         authorizationCodeExpiresAt;
   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "access_token_metadata_id")
   private AccessTokenMetadataEntity       accessTokenMetadata;
   @Column(length = 4000)
   private String                          accessTokenValue;
   private Instant                         accessTokenIssuedAt;
   private Instant                         accessTokenExpiresAt;
   private String                          accessTokenType;
   @Column(length = 1000)
   private String                          accessTokenScopes;
   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "authorization_code_metadata_id")
   private AuthorizationCodeMetadataEntity authorizationCodeMetadata;
   @Column(length = 4000)
   private String                          refreshTokenValue;
   private Instant                         refreshTokenIssuedAt;
   private Instant                         refreshTokenExpiresAt;
   @Column(length = 2000)
   private String                          refreshTokenMetadata;
   @Column(length = 4000)
   private String                          oidcIdTokenValue;
   private Instant                         oidcIdTokenIssuedAt;
   private Instant                         oidcIdTokenExpiresAt;

   @Column(length = 2000)
   private String                          oidcIdTokenMetadata;
   @Column(length = 2000)
   private String                          oidcIdTokenClaims;
}
