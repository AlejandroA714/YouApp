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
import sv.com.udb.services.authentication.repository.IPrincipalRepository;

import java.time.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UploadPhotoTask implements AuthenticationTask {
   @NonNull
   private final IMinioService        minioService;
   @NonNull
   private final IPrincipalRepository principalRepository;
   private YouAppPrincipal            principal;

   @Override
   public void setPrincipal(YouAppPrincipal principal) {
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
         principal.setPhoto(response.get(minioService.FILE_NAME).toString());
         principal.setRegistration(LocalDate.now(ZoneId.of("GMT-06:00")));
         principalRepository.save(principal);
      }
      catch (Exception e) {
         LOGGER.info("Failed to upload user image, due: {}", e.getMessage());
      }
   }
}
