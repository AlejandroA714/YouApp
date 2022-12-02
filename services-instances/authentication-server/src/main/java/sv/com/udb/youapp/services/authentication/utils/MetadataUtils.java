package sv.com.udb.youapp.services.authentication.utils;

import java.util.Collection;
import java.util.Map;

public class MetadataUtils {
   public static final String SUB                      = "sub";
   public static final String AUD                      = "aud";
   public static final String NBF                      = "nbf";
   public static final String SCOPE                    = "scope";
   public static final String ISS                      = "iss";
   public static final String EXP                      = "exp";
   public static final String IAT                      = "iat";
   public static final String ENA                      = "invalidated";
   public static final String TOKEN_METADATA_NAMESPACE = "metadata.token";
   public static final String TOKEN_INVALIDATED        = "metadata.token.invalidated";

   public static <T> T getSettings(Map<String, Object> map, String name) {
      var dd = map.get(name);
      return (T) dd;
   }

   public static <T> Collection<T> getCollectionSettings(
         Map<String, Object> map, String name) {
      var dd = map.get(name);
      return (Collection<T>) dd;
   }
}
