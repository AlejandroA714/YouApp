package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.com.udb.services.authentication.entities.GoogleAuthorizationRequest;
import sv.com.udb.services.authentication.services.IAuthenticationService;
import sv.com.udb.services.authentication.services.IGoogleService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthenticationController {
   @NonNull
   private final IAuthenticationService IAuthService;
   @NonNull
   private final IGoogleService         IGoogleService;

   @PostMapping("/google")
   public void google(
         @Valid @RequestBody GoogleAuthorizationRequest principal) {
      try {
         IGoogleService.validateToken(principal);
      }
      catch (Exception e) {
         throw e;
      }
   }
}
