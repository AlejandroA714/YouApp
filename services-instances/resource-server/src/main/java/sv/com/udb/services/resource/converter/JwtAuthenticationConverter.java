package sv.com.udb.services.resource.converter;

import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
public class JwtAuthenticationConverter
      implements Converter<Jwt, AbstractAuthenticationToken> {
   private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
   private static final String                  AUTHORITIES_CLAIM                  = "authorities";

   @Override
   public AbstractAuthenticationToken convert(final Jwt source) {
      Collection<GrantedAuthority> authorities = Stream
            .concat(defaultGrantedAuthoritiesConverter.convert(source).stream(),
                  extractResourceRoles(source).stream())
            .collect(Collectors.toSet());
      return new JwtAuthenticationToken(source, authorities);
   }

   private static Collection<? extends GrantedAuthority> extractResourceRoles(
         final Jwt jwt) {
      Collection<String> authorities = jwt.getClaim(AUTHORITIES_CLAIM);
      if (authorities != null)
         return authorities.stream().map(x -> new SimpleGrantedAuthority(x))
               .collect(Collectors.toSet());
      return Collections.emptySet();
   }
}