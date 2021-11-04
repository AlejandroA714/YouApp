package sv.com.udb.youapp.services.storage.services.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sv.com.udb.youapp.services.storage.properties.StorageProperties;
import sv.com.udb.youapp.services.storage.services.ITransferService;

import java.io.File;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
public class DefaultTransferService implements ITransferService {
   @NonNull
   private final StorageProperties properties;
   private static final String     ZIP = ".zip";
   private static final String     DOT = ".";

   @Override
   public void upload(File file) {
   }

   @Override
   public void save(byte[] bytes) {
      try {
         var now = LocalDate.now();
         var file = properties.getFileConfiguration().getMusicRepository()
               .createRelative("id" + DOT + "" + ZIP).getFile();
      }
      catch (Exception e) {
      }
   }
}
