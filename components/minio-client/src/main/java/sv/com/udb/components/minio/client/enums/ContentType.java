package sv.com.udb.components.minio.client.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {
   IMAGE_JPEG("image/jpeg", ".jpeg"), IMAGE_PNG("image/png", ".png"), IMAGE_GIF(
         "image/gif", ".gif"), AUDIO_MP3("audio/mpeg",
               ".mp3"), VIDEO_MP4("video/mp4", ".mp4");

   private final String mimeType;
   private final String ext;
}
