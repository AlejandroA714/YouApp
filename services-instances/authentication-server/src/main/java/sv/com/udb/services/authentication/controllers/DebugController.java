package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sv.com.udb.components.mail.sender.services.IEmailService;
import sv.com.udb.services.authentication.entities.Role;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.models.AbstractPrincipal;
import sv.com.udb.services.authentication.repository.IOAuthRegistrationRepository;
import sv.com.udb.services.authentication.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.repository.IPrivilegeRepository;
import sv.com.udb.services.authentication.repository.IRoleRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/test")
public class DebugController {
   @NonNull
   private final IPrincipalRepository         principalRepository;
   @NonNull
   private final IRoleRepository              roleRepository;
   @NonNull
   private final IPrivilegeRepository         privilegeRepository;
   @NonNull
   private final IOAuthRegistrationRepository registrationRepository;
   @NonNull
   private final IEmailService                emailService;

   @GetMapping(value = "/template", produces = "text/html")
   public String template(@RequestParam String template) {
      Map<String, Object> props = Map.of("id", "104918283748467935812",
            "nombres", "Victor Alejandro", "apellidos", "Alejo Galvez",
            "fullname", "Victor Alejandro Alejo Galvez", "email",
            "alejandroalejo714@gmail.com", "username", "valejo", "birthday",
            "2020-14-07", "photo",
            "https://lh3.googleusercontent.com/a-/AOh14GgA3_5dWIGw4yXVw1gaRmIIc5Qit2Qiy0RbaOjnUA=s96-c",
            "TOKEN", "b1b6a7fd-1c44-48e3-80ba-361bd14f71a5", "newpass",
            "jjis19sdasd");
      return emailService.processTemplate(template, props);
   }

   @GetMapping("/protected")
   public void ispropsd(@AuthenticationPrincipal AbstractPrincipal user) {
      Authentication auth = SecurityContextHolder.getContext()
            .getAuthentication();
      var p = auth.getPrincipal();
      var c = auth.getCredentials();
      var a = auth.getAuthorities();
      var d = auth.getDetails();
      LOGGER.info("auth: {}", auth);
   }

   @GetMapping("/users")
   public List<String> getUsers() {
      return principalRepository.findAllWithRoles().stream()
            .map(x -> x.toString()).collect(Collectors.toList());
   }

   @GetMapping("/roles")
   public List<Role> getRoles() {
      return roleRepository.findAllWithPrivileges();
   }

   @GetMapping("/roles2")
   public Object getRoles2() {
      var dd = roleRepository.findAllWithPrincipal();
      var d = dd.stream().findFirst().get();
      var x = d.getPrincipals().stream().findFirst();
      LOGGER.info("User id: {}", x.get().getId());
      return dd;
   }

   @GetMapping("/privilege")
   public Object getPrivileges() {
      var dd = privilegeRepository.findAllWithRole();
      var d = dd.stream().findFirst().get();
      var x = d.getRoles().stream().findFirst().get();
      LOGGER.info("Role Name {}", x.getName());
      return dd;
   }

   @GetMapping("/oauth")
   public Object getOauth() {
      var dd = registrationRepository.findAllWithPrincipal();
      var d = dd.stream().findFirst().get();
      var x = d.getRegistrations().stream().findFirst().get();
      LOGGER.info("User id: {}", x.getId());
      return dd;
   }
}
