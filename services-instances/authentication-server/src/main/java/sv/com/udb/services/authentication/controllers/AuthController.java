package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sv.com.udb.services.authentication.entities.User;
import sv.com.udb.services.authentication.repository.UserRepository;
import sv.com.udb.services.authentication.services.EncryptionPasswordService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

  @NonNull
  private final UserRepository userRepository;
  @NonNull
  private final EncryptionPasswordService encryptionPasswordService;

  @GetMapping("/list")
  public List<User> test(@RequestParam String code){
    LOGGER.info("code {}",code);
    //LOGGER.info("PRINCIPAL: {}",principal);
    var users = userRepository.findAll();
    return users;
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public void Conflic(Exception e){
    LOGGER.error("Failed");
  }

  @GetMapping("/test")
  public String asd() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
    return encryptionPasswordService.encryptPassword("9d[?hr%[Y>w~nV3_");
  }





}
