package sv.com.udb.youapp.services.storage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sv.com.udb.youapp.services.storage.models.UploadRequest;
import sv.com.udb.youapp.services.storage.services.ITransferService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/storage")
public class StorageController {
   private final ITransferService transferService;
   private static final String    AUTHORITIES_CLAIM = "authorities";
   private static final String    UUID_CLAIM        = "id";

   @PostMapping("/upload")
   public void upload(Authentication principal, @Valid UploadRequest request,
         @RequestParam("payload") MultipartFile file) {
      try {
         JwtAuthenticationToken authentication;
         if (principal instanceof JwtAuthenticationToken) {
            authentication = (JwtAuthenticationToken) principal;
         }
         else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                  "Bearer token is required");
         transferService.save(file.getBytes(),
               authentication.getToken().getClaimAsString(UUID_CLAIM), request);
         LOGGER.info("asd");
      }
      catch (Exception e) {
      }
   }
}
