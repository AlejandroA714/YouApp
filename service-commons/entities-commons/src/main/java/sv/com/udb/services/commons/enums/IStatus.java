package sv.com.udb.services.commons.enums;

import lombok.Getter;

@Getter
public enum IStatus {
   PENDING(1), UPLOADING(2), READY(3), FAILED(4);

   int primaryKey;

   IStatus(int primaryKey) {
      this.primaryKey = primaryKey;
   }
}
