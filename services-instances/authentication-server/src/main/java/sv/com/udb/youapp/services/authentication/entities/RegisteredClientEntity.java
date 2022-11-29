package sv.com.udb.youapp.services.authentication.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity(name = "registered_client")
public class RegisteredClientEntity {
   @Id
   private String  id;
   @Column(nullable = false)
   private String  clientId;
   @Column(nullable = false)
   private Instant clientIssuedAt;
   @Column
   private String  clientSecret;
   @Column
   private Instant clientSecretExpiresAt;
   @Column
   private String  clientName;
   @Column(length = 1000)
   private String  clientAuthenticationMethods;
   @Column(length = 1000)
   private String  authorizationGrantTypes;
   @Column(length = 1000)
   private String  redirectUris;
   @Column(length = 1000)
   private String  scopes;

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getClientId() {
      return clientId;
   }

   public void setClientId(String clientId) {
      this.clientId = clientId;
   }

   public Instant getClientIssuedAt() {
      return clientIssuedAt;
   }

   public void setClientIssuedAt(Instant clientIssuedAt) {
      this.clientIssuedAt = clientIssuedAt;
   }

   public String getClientSecret() {
      return clientSecret;
   }

   public void setClientSecret(String clientSecret) {
      this.clientSecret = clientSecret;
   }

   public Instant getClientSecretExpiresAt() {
      return clientSecretExpiresAt;
   }

   public void setClientSecretExpiresAt(Instant clientSecretExpiresAt) {
      this.clientSecretExpiresAt = clientSecretExpiresAt;
   }

   public String getClientName() {
      return clientName;
   }

   public void setClientName(String clientName) {
      this.clientName = clientName;
   }

   public String getClientAuthenticationMethods() {
      return clientAuthenticationMethods;
   }

   public void setClientAuthenticationMethods(
         String clientAuthenticationMethods) {
      this.clientAuthenticationMethods = clientAuthenticationMethods;
   }

   public String getAuthorizationGrantTypes() {
      return authorizationGrantTypes;
   }

   public void setAuthorizationGrantTypes(String authorizationGrantTypes) {
      this.authorizationGrantTypes = authorizationGrantTypes;
   }

   public String getRedirectUris() {
      return redirectUris;
   }

   public void setRedirectUris(String redirectUris) {
      this.redirectUris = redirectUris;
   }

   public String getScopes() {
      return scopes;
   }

   public void setScopes(String scopes) {
      this.scopes = scopes;
   }
   // @Column(length = 2000)
   // private String clientSettings;
   // @Column(length = 2000)
   // private String tokenSettings;
}
