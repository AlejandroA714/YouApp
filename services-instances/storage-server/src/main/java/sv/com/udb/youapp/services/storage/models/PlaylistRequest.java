package sv.com.udb.youapp.services.storage.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistRequest {
   @NotNull
   private int musicId;
   @NotNull
   private int playlistId;
}
