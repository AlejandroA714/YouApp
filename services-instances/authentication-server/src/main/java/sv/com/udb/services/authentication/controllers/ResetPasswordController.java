package sv.com.udb.services.authentication.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth/reset-password")
public class ResetPasswordController {

  @GetMapping("/")
  public String reset(){
    return "Reseting Password";
  }

}
