package sv.com.udb.services.authentication.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/private")
public class TestController {

  @GetMapping("/")
  public String authenticated(){
    return "You must be authenticated to access";
  }
}
