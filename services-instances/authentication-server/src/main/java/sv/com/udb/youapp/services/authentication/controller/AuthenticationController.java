package sv.com.udb.youapp.services.authentication.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.com.udb.youapp.services.authentication.services.EncryptPasswordService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthenticationController {
   @NonNull
   private final ModelMapper    modelMapper;
   @NonNull
   private final EncryptPasswordService encryptPasswordService;

   @GetMapping("/")
   public String dd() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
      return encryptPasswordService.encryptPassword("9d[?hr%[Y>w~nV3_");
   }
}
