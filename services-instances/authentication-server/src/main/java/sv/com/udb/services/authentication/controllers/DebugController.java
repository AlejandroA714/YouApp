package sv.com.udb.services.authentication.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sv.com.udb.services.authentication.entities.Role;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.models.AbstractPrincipal;
import sv.com.udb.services.authentication.models.Principal;
import sv.com.udb.services.authentication.repository.IOAuthRegistrationRepository;
import sv.com.udb.services.authentication.repository.IPrincipalRepository;
import sv.com.udb.services.authentication.repository.IPrivilegeRepository;
import sv.com.udb.services.authentication.repository.IRoleRepository;

import java.util.List;

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
   public List<YouAppPrincipal> getUsers() {
      return principalRepository.findAllWithRoles();
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
