package sv.com.udb.youapp.services.storage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sv.com.udb.youapp.services.storage.models.UploadRequest;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1/storage")
public class StorageController {
   @PostMapping("/upload")
   public void upload(Authentication principal, @Valid UploadRequest request,
         @RequestParam("file") MultipartFile file) {
      // JwtAuthenticationToken
      LOGGER.info("asd");
   }
}
