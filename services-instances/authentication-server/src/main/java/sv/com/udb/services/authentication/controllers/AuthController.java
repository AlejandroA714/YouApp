package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.com.udb.services.authentication.entities.User;
import sv.com.udb.services.authentication.repository.UserRepository;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

  @NonNull
  private final UserRepository userRepository;

  @GetMapping("/list")
  public List<User> test(){
    var users = userRepository.findAll();
    return users;
  }

  @GetMapping("/")
  public String auth(){
    return "You are logged in";
  }



}
