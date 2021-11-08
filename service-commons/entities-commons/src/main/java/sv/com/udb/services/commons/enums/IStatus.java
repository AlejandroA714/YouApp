package sv.com.udb.services.commons.enums;

import lombok.Getter;

@Getter
public enum IStatus {
   PENDING(1), UPLOADING(2), INCOMPLETE(3), READY(4), FAILED(5);

   int primaryKey;

   IStatus(int primaryKey) {
      this.primaryKey = primaryKey;
   }
}
