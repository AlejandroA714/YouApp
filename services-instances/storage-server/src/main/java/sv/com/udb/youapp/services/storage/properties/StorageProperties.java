package sv.com.udb.youapp.services.storage.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotNull;
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
      private Resource             musicRepository     = new FileSystemResource(
            "/tmp/music/");
      private String               filterExpresion     = ".*\\.(zip)$";
      private Duration             transportTaskPeriod = Duration.ofSeconds(30);
      private Duration             deleteTaskPeriod    = Duration.ofMinutes(1);
      private ZipFileConfiguration artWork             = new ZipFileConfiguration(
            true, "artwork.jpg", "ArtWork from the song");
      private ZipFileConfiguration payload             = new ZipFileConfiguration(
            true, "payload.mp3", "Song to be upload");
      private ZipFileConfiguration information         = new ZipFileConfiguration(
            true, "information.json", "Information about the song");
   }

   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public static class ZipFileConfiguration {
      private boolean enabled = true;
      @NotNull
      private String  name;
      @NotNull
      private String  comment;

      public String getFileName() {
         return FilenameUtils.removeExtension(FilenameUtils.getName(this.name));
      }
   }
}
