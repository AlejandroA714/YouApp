package sv.com.udb.components.minio.client.services.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import sv.com.udb.components.minio.client.enums.ContentType;
import sv.com.udb.components.minio.client.properties.MinioClientProperties;
import sv.com.udb.components.minio.client.services.IMinioService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@Getter
@RequiredArgsConstructor
public class DefaultMinioService implements IMinioService {
   @NonNull
   private final MinioClient           minioClient;
   @NonNull
   private final MinioClientProperties properties;

   @Override
   public JSONObject upload(byte[] byteArray, String suffix,
         ContentType contentType) {
      try (ByteArrayInputStream stream = new ByteArrayInputStream(byteArray)) {
         return upload(stream, suffix, (long) byteArray.length, contentType);
      }
      catch (Exception e) {
         return null;
      }
   }

   public byte[] getFile(String key) {
      try (InputStream stream = minioClient.getObject(GetObjectArgs.builder()
            .bucket(properties.getBucket()).object(key).build())) {
         return stream.readAllBytes();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   @Override
   public Logger getLogger() {
      return DefaultMinioService.LOGGER;
   }
}
