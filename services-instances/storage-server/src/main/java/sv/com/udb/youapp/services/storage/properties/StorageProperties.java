package sv.com.udb.youapp.services.storage.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.time.Duration;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class StorageProperties {
   private FileConfiguration fileConfiguration = new FileConfiguration();

   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public static class FileConfiguration {
      private Resource musicRepository     = new FileSystemResource(
            "/tmp/music/");
      private String   filterExpresion     = ".*\\.(mp3|ogg)$";
      private Duration transportTaskPeriod = Duration.ofMinutes(1);
      private Duration deleteTaskPeriod    = Duration.ofMinutes(1);
   }
}
