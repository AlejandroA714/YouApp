package sv.com.udb.services.authentication.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
   private String oldPassword;
   private String newPassword;
   private String repeatPassword;
}
