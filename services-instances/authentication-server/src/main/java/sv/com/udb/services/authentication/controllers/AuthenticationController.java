package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.exceptions.InvalidTokenException;
import sv.com.udb.services.authentication.exceptions.RegistrationException;
import sv.com.udb.services.authentication.models.AbstractPrincipal;
import sv.com.udb.services.authentication.services.IAuthenticationService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthenticationController {
   @NonNull
   private final IAuthenticationService authService;

   @GetMapping(value = "/confirm_email", produces = "text/html")
   public String validateToken(@RequestParam String token) {
      try {
         return authService.validateToken(token);
      }
      catch (Exception e) {
         LOGGER.error("Failed to activate user, due: {}", e);
         throw new InvalidTokenException(e);
      }
   }

   @PostMapping("/register")
   public YouAppPrincipal register(
         @Valid @RequestBody AbstractPrincipal principal) {
      try {
         LOGGER.trace("Register: {}", principal);
         return authService.register(principal);
      }
      catch (Exception e) {
         LOGGER.error("Failed to register, due: {}", e.getMessage());
         throw new RegistrationException(e.getMessage(), e);
      }
   }
}
