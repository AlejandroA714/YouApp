package sv.com.udb.services.authentication.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.*;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import sv.com.udb.services.authentication.models.GoogleAuthorizationRequest;
import sv.com.udb.services.authentication.models.OAuth2TokenResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class OAuth2GoogleEndpointFilter extends OncePerRequestFilter {
   private static final String           DEFAULT_AUTHORIZATION_ENDPOINT_URI = "/v1/auth/google/";
   @NonNull
   private final AuthenticationManager   authenticationManager;
   @NonNull
   private final RequestMatcher          authorizationEndpointMatcher;
   @NonNull
   private final ObjectMapper            objectMapper;
   @NonNull
   private final AuthenticationConverter authenticationConverter;

   public OAuth2GoogleEndpointFilter(@NonNull AuthenticationManager authManager,
         @NonNull ObjectMapper mapper) {
      this(authManager, DEFAULT_AUTHORIZATION_ENDPOINT_URI, mapper);
   }

   public OAuth2GoogleEndpointFilter(@NonNull AuthenticationManager authManager,
         @NonNull String authorizationEndpointUri,
         @NonNull ObjectMapper mapper) {
      this.authenticationManager = authManager;
      this.authorizationEndpointMatcher = createDefaultRequestMatcher(
            authorizationEndpointUri);
      this.authenticationConverter = new OAuth2GoogleAuthenticationConverter(
            mapper);
      this.objectMapper = mapper;
   }

   @Override
   protected void doFilterInternal(HttpServletRequest request,
         HttpServletResponse response, FilterChain filterChain)
         throws ServletException, IOException {
      LOGGER.info("in endpoint filter");
      if (!this.authorizationEndpointMatcher.matches(request)) {
         filterChain.doFilter(request, response);
         return;
      }
      try {
         GoogleAuthorizationRequest authorizationRequest = (GoogleAuthorizationRequest) this.authenticationConverter
               .convert(request);
         OAuth2AccessTokenAuthenticationToken accessToken = (OAuth2AccessTokenAuthenticationToken) this.authenticationManager
               .authenticate(authorizationRequest);
         sendResponse(objectMapper.writeValueAsString(OAuth2TokenResponse
               .builder()
               .access_token(accessToken.getAccessToken().getTokenValue())
               .refresh_token(accessToken.getRefreshToken().getTokenValue())
               .expires_in(accessToken.getAccessToken().getExpiresAt())
               .scope(accessToken.getAccessToken().getScopes().stream()
                     .reduce("", String::concat))
               .token_type("Bearer").build()), response);
         return;
      }
      catch (Exception e) {
         throw e;
      }
   }

   private void sendResponse(String body, HttpServletResponse response) {
      try {
         response.resetBuffer();
         response.setStatus(HttpStatus.OK.value());
         response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
         response.getOutputStream().print(body);
      }
      catch (Exception e) {
         throw new ResponseStatusException(HttpStatus.OK.value(),
               "Failed to login with google", e.getCause());
      }
   }

   private static RequestMatcher createDefaultRequestMatcher(
         String authorizationEndpointUri) {
      RequestMatcher authorizationRequestGetMatcher = new AntPathRequestMatcher(
            authorizationEndpointUri, HttpMethod.GET.name());
      RequestMatcher authorizationRequestPostMatcher = new AntPathRequestMatcher(
            authorizationEndpointUri, HttpMethod.POST.name());
      RequestMatcher openidScopeMatcher = request -> {
         String scope = request.getParameter(OAuth2ParameterNames.SCOPE);
         return StringUtils.hasText(scope) && scope.contains(OidcScopes.OPENID);
      };
      RequestMatcher responseTypeParameterMatcher = request -> request
            .getParameter(OAuth2ParameterNames.RESPONSE_TYPE) != null;
      RequestMatcher authorizationRequestMatcher = new OrRequestMatcher(
            authorizationRequestGetMatcher,
            new AndRequestMatcher(authorizationRequestPostMatcher,
                  responseTypeParameterMatcher, openidScopeMatcher));
      RequestMatcher authorizationConsentMatcher = new AndRequestMatcher(
            authorizationRequestPostMatcher,
            new NegatedRequestMatcher(responseTypeParameterMatcher));
      return new OrRequestMatcher(authorizationRequestMatcher,
            authorizationConsentMatcher);
   }
}
