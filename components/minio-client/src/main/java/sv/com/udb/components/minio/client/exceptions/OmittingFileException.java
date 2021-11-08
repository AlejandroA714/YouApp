package sv.com.udb.components.minio.client.exceptions;

public class OmittingFileException extends RuntimeException{
   public OmittingFileException(String message) {
      super(message);
   }

   public OmittingFileException(String message, Throwable cause) {
      super(message, cause);
   }
}
