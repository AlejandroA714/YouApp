package sv.com.udb.youapp.services.storage.services;

import sv.com.udb.components.minio.client.exceptions.TransferException;
import sv.com.udb.services.commons.entities.Music;
import sv.com.udb.youapp.services.storage.models.UploadRequest;

import java.io.File;

public interface ITransferService {
   void upload(File file) throws TransferException;

   Music save(byte[] bytes, String id, UploadRequest request);
}
