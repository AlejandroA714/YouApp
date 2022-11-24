package sv.com.udb.youapp.services.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import sv.com.udb.youapp.services.authentication.enums.OAuth2Registration;
import sv.com.udb.youapp.services.authentication.enums.OAuth2Role;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {
   private String             id;
   @NotBlank
   @Size(max = 32)
   private String             nombres;
   @NotBlank
   @Size(max = 32)
   private String             apellidos;
   @NotBlank
   @Size(max = 32)
   private String             username;
   @Email
   @NotBlank
   @Size(max = 48)
   private String             email;
   @NotNull
   @PastOrPresent
   private LocalDate          birthday;
   private LocalDate          registrationDate;
   private boolean            isActive;
   private OAuth2Registration registrationType;
   private OAuth2Role         role;
}
