package sv.com.udb.components.minio.client.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@ToString
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class MinioClientProperties {
   @URL
   @NotNull
   private String url;
   @NotNull
   private String accessKey;
   @NotNull
   private String secretKey;
   @NotNull
   private String bucket;

   public String getUri(String fileName) {
      return String.format("%s/%s/%s", url, bucket, fileName);
   }
}
