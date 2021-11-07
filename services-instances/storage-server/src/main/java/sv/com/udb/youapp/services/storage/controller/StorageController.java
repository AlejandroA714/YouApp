package sv.com.udb.youapp.services.storage.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sv.com.udb.services.commons.entities.Music;
import sv.com.udb.services.commons.entities.YouAppPrincipal;
import sv.com.udb.services.commons.exceptions.InvalidAuthenticationException;
import sv.com.udb.services.commons.exceptions.PrincipalDoesNotExist;
import sv.com.udb.services.commons.repository.IPrincipalRepository;
import sv.com.udb.youapp.services.storage.models.UploadRequest;
import sv.com.udb.youapp.services.storage.services.ITransferService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/storage")
public class StorageController {
   @NonNull
   private final IPrincipalRepository principalRepository;
   private final ITransferService     transferService;
   private static final String        AUTHORITIES_CLAIM = "authorities";
   private static final String        UUID_CLAIM        = "id";

   @GetMapping(value = "/me")
   public YouAppPrincipal me(Authentication authentication) {
      JwtAuthenticationToken principal;
      if (authentication instanceof JwtAuthenticationToken) {
         principal = (JwtAuthenticationToken) authentication;
         Optional<YouAppPrincipal> p = principalRepository.findByIdWithRoles(
               principal.getToken().getClaimAsString(UUID_CLAIM));
         if (!p.isPresent()) {
            throw new PrincipalDoesNotExist("asd" + " does not exits");
         }
         return p.get();
      }
      else {
         throw new InvalidAuthenticationException("Auhentication no valid");
      }
   }

   @PostMapping("/upload")
   public Music upload(@RequestParam("file") MultipartFile file,
         @Valid UploadRequest request, Authentication principal)
         throws IOException {
      try {
         JwtAuthenticationToken authentication;
         if (principal instanceof JwtAuthenticationToken) {
            authentication = (JwtAuthenticationToken) principal;
         }
         else
            throw new InvalidAuthenticationException(
                  "Bearer token is required");
         return transferService.save(file.getBytes(),
               authentication.getToken().getClaimAsString(UUID_CLAIM), request);
      }
      catch (Exception e) {
         throw e;
      }
   }
}
