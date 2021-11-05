package sv.com.udb.components.minio.client.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import sv.com.udb.components.minio.client.enums.ContentType;
import sv.com.udb.components.minio.client.exceptions.TransferException;
import sv.com.udb.components.minio.client.properties.MinioClientProperties;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public interface IMinioService {
   String FILE_NAME = "fileName";

   MinioClient getMinioClient();

   MinioClientProperties getProperties();

   Logger getLogger();

   JSONObject upload(byte[] byteArray, String suffix, ContentType contentType)
         throws TransferException;

   byte[] getFile(String fileName);

   default JSONObject upload(InputStream stream, String suffix, Long size,
         ContentType contentType) throws Exception {
      JSONObject map = new JSONObject();
      String objectName = UUID.randomUUID()
            + (suffix != null ? "_" + suffix : "") + contentType.getExt();
      PutObjectArgs put = new PutObjectArgs.Builder()
            .contentType(contentType.getMimeType())
            .bucket(getProperties().getBucket()).object(objectName)
            .stream(stream, size, -1).build();
      try {
         var res = getMinioClient().putObject(put);
         getLogger().info("{}", res);
      }
      catch (Exception e) {
         getLogger().error(e.getMessage());
         e.printStackTrace();
      }
      map.put(FILE_NAME, getProperties().getUri(objectName));
      return map;
   }
}
