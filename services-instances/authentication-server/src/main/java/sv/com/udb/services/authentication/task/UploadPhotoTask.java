package sv.com.udb.services.authentication.task;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sv.com.udb.components.minio.client.enums.ContentType;
import sv.com.udb.components.minio.client.services.IMinioService;
import sv.com.udb.services.authentication.entities.YouAppPrincipal;
import sv.com.udb.services.authentication.models.Principal;
import sv.com.udb.services.authentication.repository.IPrincipalRepository;

import java.time.*;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadPhotoTask implements AuthenticationTask {
   @NonNull
   private final IMinioService        minioService;
   @NonNull
   private final IPrincipalRepository principalRepository;
   private Principal                  principal;

   @Override
   public void setPrincipal(Principal principal) {
      this.principal = principal;
   }

   @Override
   public void run() {
      try {
         if (principal.getPhoto().equals("") || principal.getPhoto() == null)
            return;
         byte[] byteArray = Base64.decode(principal.getPhoto());
         var response = minioService.upload(byteArray, "",
               ContentType.IMAGE_JPEG);
         var p = principalRepository.findById(principal.getId());
         if (!p.isPresent()) {
            LOGGER.error("User not exist in db with id: {}", principal.getId());
            return;
         }
         var youapp = p.get();
         youapp.setPhoto(response.get(minioService.FILE_NAME).toString());
         principalRepository.save(youapp);
      }
      catch (Exception e) {
         LOGGER.info("Failed to upload user image, due: {}", e.getMessage());
      }
   }
}
