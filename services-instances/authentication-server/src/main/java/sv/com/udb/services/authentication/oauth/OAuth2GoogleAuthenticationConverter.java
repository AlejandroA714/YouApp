package sv.com.udb.services.authentication.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import sv.com.udb.services.authentication.entities.GoogleAuthorizationRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class OAuth2GoogleAuthenticationConverter
      implements AuthenticationConverter {
   @NonNull
   private final ObjectMapper mapper;

   @Override
   public Authentication convert(HttpServletRequest request) {
      try {
         String requestData = request.getReader().lines()
               .collect(Collectors.joining());
         return mapper.readValue(requestData, GoogleAuthorizationRequest.class);
      }
      catch (Exception e) {
         LOGGER.error("Failed to convert request due: {}", e.getMessage());
         throw new RuntimeException("Invalid Request!");
      }
   }
}
