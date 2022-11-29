package sv.com.udb.youapp.services.authentication.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.com.udb.youapp.commons.jpa.services.PrincipalService;
import sv.com.udb.youapp.services.authentication.repositories.JpaClientRepository;
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
   private final EncryptPasswordService encryptPasswordService;
   @NonNull
   private final PrincipalService       service;
   @NonNull
   private final JpaClientRepository    clientRepository;

   @GetMapping("/")
   public Object dd() throws NoSuchPaddingException, IllegalBlockSizeException,
         NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException,
         InvalidKeyException {
      var dd = service.findAll();
      var sdsd = clientRepository.findAll();
      LOGGER.info("User size: {}", dd.size());
      LOGGER.info("Client {}", sdsd.size());
      return sdsd;
      // return encryptPasswordService.encryptPassword("9d[?hr[YDwFnV3_");
   }
}
