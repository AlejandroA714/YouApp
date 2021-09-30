package sv.com.udb.services.authentication.entities;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class GooglePrincipal {
   @NotNull
   private String id;
   @NotNull
   private String email;
   @NotNull
   private String familyName;
   @NotNull
   private String givenName;
   @NotNull
   private String name;
   private String photo;
}
