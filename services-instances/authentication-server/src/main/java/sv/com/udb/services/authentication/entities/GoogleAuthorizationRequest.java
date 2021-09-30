package sv.com.udb.services.authentication.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class GoogleAuthorizationRequest implements Serializable {
   @NotNull
   private String          idToken;
   @NotNull
   private String          accessToken;
   @NotNull
   private List<String>    scopes;
   @Valid
   @NotNull
   @JsonProperty("user")
   private GooglePrincipal principal;
   private String          serverAuthCode;
}
