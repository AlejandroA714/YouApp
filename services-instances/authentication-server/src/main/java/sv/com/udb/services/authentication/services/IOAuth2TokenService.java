package sv.com.udb.services.authentication.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

public interface IOAuth2TokenService {
   void setJwtCustomizer(
         OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer);

   OAuth2AccessTokenAuthenticationToken getAcessToken(Authentication auth);
}
