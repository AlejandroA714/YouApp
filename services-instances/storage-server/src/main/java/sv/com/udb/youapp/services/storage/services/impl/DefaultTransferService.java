package sv.com.udb.youapp.services.storage.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import sv.com.udb.components.minio.client.enums.ContentType;
import sv.com.udb.components.minio.client.exceptions.OmittingFileException;
import sv.com.udb.components.minio.client.exceptions.TransferException;
import sv.com.udb.components.minio.client.services.IMinioService;
import sv.com.udb.services.commons.entities.Genre;
import sv.com.udb.services.commons.entities.Music;
import sv.com.udb.services.commons.entities.Status;
import sv.com.udb.services.commons.entities.YouAppPrincipal;
import sv.com.udb.services.commons.exceptions.PrincipalDoesNotExist;
import sv.com.udb.services.commons.repository.IGenreRepository;
import sv.com.udb.services.commons.repository.IMusicRepository;
import sv.com.udb.services.commons.repository.IPrincipalRepository;
import sv.com.udb.services.commons.repository.IStatusRepository;
import sv.com.udb.youapp.services.storage.exceptions.JsonProcessingException;
import sv.com.udb.youapp.services.storage.exceptions.UncompressionException;
import sv.com.udb.youapp.services.storage.exceptions.UploadException;
import sv.com.udb.youapp.services.storage.models.UploadRequest;
import sv.com.udb.youapp.services.storage.properties.StorageProperties;
import sv.com.udb.youapp.services.storage.properties.StorageProperties.ZipFileConfiguration;
import sv.com.udb.youapp.services.storage.services.ITransferService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Slf4j
@RequiredArgsConstructor
public class DefaultTransferService implements ITransferService {
   @NonNull
   private final StorageProperties    properties;
   @NonNull
   private final ObjectMapper         objectMapper;
   @NonNull
   private final IPrincipalRepository principalRepository;
   @NonNull
   private final IMinioService        minioService;
   @NonNull
   private final IMusicRepository     musicRepository;
   @NonNull
   private final IGenreRepository     genreRepository;
   @NonNull
   private final IStatusRepository    statusRepository;
   private static final String        ZIP            = "zip";
   private static final String        DOT            = ".";
   private static final String        SEPARATOR      = "_";
   private static final String        MUSIC_ID_CLAIM = "music_id";
   private static final String        UUID_CLAIM     = "id";
   private static final byte[]        EMPTY          = new byte[0];

   @Override
   public void upload(File file) throws TransferException {
      try {
         Map<String, byte[]> payload = unzip(file);
         JsonNode node = byteArrayToJsonNode(payload.getOrDefault(
               properties.getFileConfiguration().getInformation().getFileName(),
               EMPTY));
         Optional<Music> _song = musicRepository
               .findById(node.get(MUSIC_ID_CLAIM).asInt());
         if (!_song.isPresent())
            throw new TransferException("That song doest not exist");
         Music song = _song.get();
         song.setStatus(statusRepository.getById(2));
         musicRepository.save(song);
         byte[] byteSong = payload.getOrDefault(
               properties.getFileConfiguration().getPayload().getFileName(),
               EMPTY);
         try{
            JSONObject response = minioService.upload(byteSong,
                String.valueOf(Instant.now().getEpochSecond()),
                ContentType.AUDIO_MP3);
            song.setUri(response.get(IMinioService.FILE_NAME).toString());
            song.setStatus(statusRepository.getById(4));
         }catch (OmittingFileException ex){
            song.setStatus(statusRepository.getById(3));
            LOGGER.warn("Music File has been ommited");
         }
         byte[] artWork = payload.getOrDefault(
               properties.getFileConfiguration().getArtWork().getFileName(),
               EMPTY);
         try{
            JSONObject photo = minioService.upload(artWork,
                String.valueOf(Instant.now().getEpochSecond()),
                ContentType.IMAGE_JPEG);
            song.setPhoto(photo.get(IMinioService.FILE_NAME).toString());
         }catch (OmittingFileException ex){
            song.setStatus(statusRepository.getById(3));
            LOGGER.warn("Art Work has been ommited");
         }
         musicRepository.save(song);
      }
      catch (Exception e) {
         throw new TransferException(e.getMessage(), e);
      }
   }

   @Override
   public Music save(byte[] bytes, String uuid, UploadRequest request) {
      try {
         Optional<YouAppPrincipal> principal = principalRepository
               .findById(uuid);
         if (!principal.isPresent())
            throw new PrincipalDoesNotExist("User does not exits");
         Music m = createMusic(uuid, request);
         Map<String, Object> summary = principal.get().getSummary();
         summary.put(MUSIC_ID_CLAIM, m.getId());
         var file = properties.getFileConfiguration().getMusicRepository()
               .createRelative(uuid + SEPARATOR + m.getId() + DOT + ZIP)
               .getFile();
         Files.createParentDirs(file);
         try (ZipOutputStream zip = new ZipOutputStream(
               new FileOutputStream(file))) {
            sendToZip(bytes, zip,
                  properties.getFileConfiguration().getPayload());
            sendToZip(summary, zip,
                  properties.getFileConfiguration().getInformation());
            if (request.getPhoto() != null && request.getPhoto().length() > 0) {
               byte[] artWorkArray = Base64.decode(request.getPhoto());
               sendToZip(artWorkArray, zip,
                     properties.getFileConfiguration().getArtWork());
            }
         }
         catch (Exception e) {
            LOGGER.error("Failed to create zip", e);
         }
         return musicRepository.findById(m.getId()).get();
      }
      catch (Exception e) {
         LOGGER.error("Failed to save music", e);
         throw new UploadException(e.getMessage(), e);
      }
   }

   private Music createMusic(String uuid, UploadRequest request) {
      YouAppPrincipal p = principalRepository.getById(uuid);
      Status status = statusRepository.getById(1);
      Genre genre = genreRepository.getById(request.getGenreId());
      Music m = Music.builder().title(request.getTitle())
            .duration(request.getDuration()).uri("").genre(genre).status(status)
            .user(p).photo("").build();
      musicRepository.save(m);
      return m;
   }

   private static Map<String, byte[]> unzip(File file)
         throws UncompressionException {
      try (ZipFile zFile = new ZipFile(file)) {
         Map<String, byte[]> payload = new HashMap<>();
         zFile.stream().forEach(zipEntry -> {
            String name = FilenameUtils
                  .removeExtension(FilenameUtils.getName(zipEntry.getName()));
            try {
               byte[] input = zFile.getInputStream(zipEntry).readAllBytes();
               payload.put(name, input);
            }
            catch (IOException e) {
               e.printStackTrace();
            }
         });
         return payload;
      }
      catch (Exception e) {
         LOGGER.error("Failed to read zip file", e);
         throw new UncompressionException(e.getMessage(), e);
      }
   }

   public JsonNode byteArrayToJsonNode(byte[] source)
         throws JsonProcessingException {
      try {
         if (source.length == 0)
            throw new JsonProcessingException("Data is null");
         return objectMapper.readTree(source);
      }
      catch (Exception e) {
         LOGGER.warn("Failed to read JSON data from byte[]", e);
         throw new JsonProcessingException(e.getMessage(), e);
      }
   }

   private void sendToZip(byte[] content, ZipOutputStream zip,
         ZipFileConfiguration configuration) {
      if (!configuration.isEnabled() || content == null
            || content.length == 0) {
         return;
      }
      try {
         ZipEntry faceE = new ZipEntry(configuration.getName());
         faceE.setCreationTime(
               FileTime.fromMillis(LocalDate.now().toEpochDay()));
         zip.setComment(configuration.getComment());
         zip.putNextEntry(faceE);
         zip.write(content);
      }
      catch (Exception e) {
         LOGGER.error("Failed to append file to zip ", e);
      }
   }

   private void sendToZip(Map<String, Object> data, ZipOutputStream zip,
         ZipFileConfiguration config) {
      try {
         sendToZip(objectMapper.writeValueAsBytes(data), zip, config);
      }
      catch (Exception e) {
         LOGGER.error("Ocurrio un error", e);
      }
   }
}
