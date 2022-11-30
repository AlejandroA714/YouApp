package sv.com.udb.youapp.services.authentication.enums;

import java.util.Locale;

public enum GrantTypeEnum {
   AUTHORIZATION_CODE, REFRESH_TOKEN, CLIENT_CREDENTIALS, PASSWORD, JWT_BEARER;

   public String val() {
      return name().toLowerCase(Locale.ROOT);
   }
}
