package sv.com.udb.services.authentication.enums;

import lombok.Getter;

@Getter
public enum IOAuthRegistrationType {
   YOUAPP(1), GOOGLE(2), OTHER(3);

   int primaryKey;

   IOAuthRegistrationType(int primaryKey) {
      this.primaryKey = primaryKey;
   }
}
