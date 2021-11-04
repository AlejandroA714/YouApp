package sv.com.udb.services.commons.enums;

import lombok.Getter;

@Getter
public enum IPrivilege {
   READ_PRIVILEGE(1), WRITE_PRIVILEGE(2), PERMISSIONS_PRIVILEGE(3), TRUST(4);

   int primaryKey;

   IPrivilege(int primaryKey) {
      this.primaryKey = primaryKey;
   }
}
