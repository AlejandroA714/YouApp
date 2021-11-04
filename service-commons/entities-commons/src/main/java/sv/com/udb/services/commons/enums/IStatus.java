package sv.com.udb.services.commons.enums;

import lombok.Getter;

@Getter
public enum IStatus {
   PENDING(1), UPLOADING(2), FAILED(3), PROCESSING(4), FINISH(5);

   int primaryKey;

   IStatus(int primaryKey) {
      this.primaryKey = primaryKey;
   }
}
