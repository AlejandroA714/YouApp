package sv.com.udb.youapp.services.storage.exceptions;

public class JsonProcessingException extends Exception {
   public JsonProcessingException(String message) {
      super(message);
   }

   public JsonProcessingException(String message, Throwable cause) {
      super(message, cause);
   }
}
