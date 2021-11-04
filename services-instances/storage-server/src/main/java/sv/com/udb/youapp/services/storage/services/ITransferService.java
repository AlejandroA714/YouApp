package sv.com.udb.youapp.services.storage.services;

import java.io.File;

public interface ITransferService {
   void upload(File file);

   void save(byte[] bytes);
}
