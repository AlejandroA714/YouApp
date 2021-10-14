package sv.com.udb.components.minio.client.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import sv.com.udb.components.minio.client.properties.MinioClientProperties;

import java.io.InputStream;
import java.util.UUID;

public interface IMinioService {
   MinioClient getMinioClient();

   MinioClientProperties getProperties();

   Logger getLogger();

   JSONObject upload(byte[] byteArray, String suffix, String contentType)
         throws Exception;

   byte[] getFile(String fileName);

   default JSONObject upload(InputStream stream, String suffix, Long size,
         String contentType) throws Exception {
      JSONObject map = new JSONObject();
      String objectName = UUID.randomUUID() + (suffix != null ? suffix : "");
      PutObjectArgs put = new PutObjectArgs.Builder().contentType(contentType)
            .bucket(getProperties().getBucket()).object(objectName)
            .stream(stream, size, -1).build();
      try {
         getMinioClient().putObject(put);
      }
      catch (Exception e) {
         getLogger().error(e.getMessage());
         e.printStackTrace();
      }
      map.put("name", objectName);
      return map;
   }
}
