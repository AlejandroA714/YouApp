package sv.com.udb.youapp.services.authentication.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import sv.com.udb.youapp.commons.jpa.enums.OAuth2Registration;
import sv.com.udb.youapp.commons.jpa.enums.OAuth2Role;

import java.time.LocalDate;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
final class YouAppPrincipalMixIn {
   @JsonCreator
   YouAppPrincipalMixIn(@JsonProperty("id") String id,
         @JsonProperty("nombres") String nombres,
         @JsonProperty("apellidos") String apellidos,
         @JsonProperty("username") String username,
         @JsonProperty("email") String email,
         @JsonProperty("password") String password,
         @JsonProperty("birthday") LocalDate birthday,
         @JsonProperty("registrationDate") LocalDate registrationDate,
         @JsonProperty("photo") String photo,
         @JsonProperty("isActive") boolean active,
         @JsonProperty("registrationType") OAuth2Registration registration,
         @JsonProperty("role") OAuth2Role role) {
   }
}
