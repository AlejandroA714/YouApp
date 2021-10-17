package sv.com.udb.services.authentication.enums;

import lombok.Getter;

@Getter
public enum IRole {
   ROLE_ADMIN(1), ROLE_MANTAINER(2), ROLE_USER(3);

   int primaryKey;

   IRole(int primaryKey) {
      this.primaryKey = primaryKey;
   }
}
