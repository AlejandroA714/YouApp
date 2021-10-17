package sv.com.udb.services.authentication.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.validation.annotation.Validated;
import sv.com.udb.services.authentication.task.AuthenticationTask;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Data
@Validated
@NoArgsConstructor
public class AuthenticationProperties {
   @NotNull
   private AuthKeys                                  keys;
   @NotNull
   private JWTConfiguration                          jwt;
   @NotNull
   private GoogleConfiguration                       google;
   @NotNull
   private ClientConfiguration                       client;
   @NotNull
   private List<String>                              redirectUris;
   @NotNull
   private String                                    roleHierarchy;
   private List<Class<? extends AuthenticationTask>> postCreationTasks = new ArrayList<>();

   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public static class ClientConfiguration {
      @NotNull
      private String                           ClientId;
      @NotNull
      private String                           ClientSecret;
      @NotNull
      private List<ClientAuthenticationMethod> AuthenticationMethods;
      @NotNull
      private List<AuthorizationGrantType>     GrantTypes;
   }

   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public static class AuthKeys {
      @NotNull
      private String privateKey;
      @NotNull
      private String publicKey;
   }

   @Data
   @NoArgsConstructor
   public static class JWTConfiguration {
      @NotNull
      private Duration access_token;
      @NotNull
      private Duration refresh_token;
   }

   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public static class GoogleConfiguration {
      @NotNull
      private String       personFields = "birthdays,genders";
      @NotNull
      private List<String> clientId;
      @NotNull
      private String       resourceName = "people/me";
   }
}
