package sv.com.udb.youapp.services.authentication.enums;

import java.util.Locale;

public enum AuthenticationMethodEnum {
   CLIENT_SECRET_BASIC, CLIENT_SECRET_POST, CLIENT_SECRET_JWT, PRIVATE_KEY_JWT, NONE;

   public String val() {
      return name().toLowerCase(Locale.ROOT);
   }
}
