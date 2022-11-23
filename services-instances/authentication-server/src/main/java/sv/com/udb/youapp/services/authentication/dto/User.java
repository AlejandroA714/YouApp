package sv.com.udb.youapp.services.authentication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import sv.com.udb.youapp.services.authentication.enums.OAuth2Registration;
import sv.com.udb.youapp.services.authentication.enums.OAuth2Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {
   private String    id;
   @NotBlank
   @Size(max = 32)
   private String    nombres;
   @NotBlank
   @Size(max = 32)
   private String    apellidos;
   @NotBlank
   @Size(max = 32)
   private String    username;
   @Email
   @NotBlank
   @Size(max = 48)
   private String    email;
   @NotNull
   @PastOrPresent
   private LocalDate birthday;
   private LocalDate registrationDate;
   private boolean   isActive;
   private OAuth2Registration registrationType;
   private OAuth2Role role;
}
