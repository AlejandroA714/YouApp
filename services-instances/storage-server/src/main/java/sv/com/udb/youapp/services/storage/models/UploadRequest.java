package sv.com.udb.youapp.services.storage.models;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class UploadRequest {
   @NotNull
   private String title;
   @NotNull
   private int    genreId;
   private int    duration;
   private String photo;
}
