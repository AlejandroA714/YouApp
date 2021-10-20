package sv.com.udb.services.authentication.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
final class AbstractPrincipalMixin {
   @JsonCreator
   AbstractPrincipalMixin(@JsonProperty("nombres") String nombres,
         @JsonProperty("apellidos") String apellidos,
         @JsonProperty("email") String email,
         @JsonProperty("username") String username,
         @JsonProperty("passwrord") String password,
         @JsonProperty("birthday") LocalDate birthday,
         @JsonProperty("photo") String photo, @JsonProperty("id") String id,
         @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities,
         @JsonProperty("active") boolean active) {
   }
}
