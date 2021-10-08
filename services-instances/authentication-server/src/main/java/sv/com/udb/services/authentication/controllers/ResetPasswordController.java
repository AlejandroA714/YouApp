package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.com.udb.services.authentication.services.IEncryptionPasswordService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth/reset-password")
public class ResetPasswordController {

   @NonNull
   private final IEncryptionPasswordService passwordService;

   @GetMapping("/")
   public String reset() {
      return "Reseting Password";
   }

   @GetMapping("/decrypt")
   public String ff(){
      try {
         return passwordService.decryptPassword("F3Y77xuxcSewvtqHH1XCg4ARZxx8HGcVW/+tzMMJ/JURS9rj8qFQUZvdHvqkes/wk/LesaQNYxo1cul9zqVN25E000V2wm1m5qgI1pC4vLUGxN7RNIh7IxIXhnVH8FIvVebHzv48D+pg7B8BrDzpySeDXEBc2G2B7BhXiDgPodBm+dO6F1xe2zE1NgNMPIEkyhlNledPRAVKKyn4naZmPy6+Y79M2lh1X8+XMD8JvEET1/aHnZ/L50F6vXiPmLaCwft0HOV313TmEj9m6pkHx1G3ON1khNmdbwLWggjskicavV14tRpZnHgB0jPIHYP2dzKydRr4Mf+6hj901hsC5g==");
      } catch (Exception e) {
         e.printStackTrace();
         return "";
      }
   }

}
