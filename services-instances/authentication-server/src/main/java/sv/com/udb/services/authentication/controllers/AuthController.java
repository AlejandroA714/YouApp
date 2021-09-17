package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.com.udb.services.authentication.entities.User;
import sv.com.udb.services.authentication.repository.UserRepository;
import sv.com.udb.services.authentication.services.EncryptionPasswordService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
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
  //@NonNull
  //private final AuthenticationManager authenticationManager;


  @GetMapping("/list")
  public List<User> test(){
    var users = userRepository.findAll();
    return users;
  }

  @GetMapping("/test")
  public String asd() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
    return encryptionPasswordService.encryptPassword("pass");
  }





}
