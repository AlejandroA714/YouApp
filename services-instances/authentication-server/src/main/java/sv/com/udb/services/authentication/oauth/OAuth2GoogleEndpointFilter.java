package sv.com.udb.services.authentication.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.*;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import sv.com.udb.services.authentication.models.GoogleAuthorizationRequest;

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
   private final RedirectStrategy        redirectStrategy                   = new DefaultRedirectStrategy();
   private AuthenticationSuccessHandler  authenticationSuccessHandler       = this::sendAuthorizationResponse;

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
         sendResponse(HttpStatus.OK,
               objectMapper.writeValueAsString(accessToken.getAccessToken()),
               response);
         return;
      }
      catch (Exception e) {
         throw e;
      }
   }

   private void sendResponse(HttpStatus status, String body,
         HttpServletResponse response) {
      try {
         response.resetBuffer();
         response.setStatus(status.value());
         response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
         response.getOutputStream().print(body);
      }
      catch (Exception e) {
         throw new ResponseStatusException(status,
               "Failed to login with google", e.getCause());
      }
   }

   private void sendAuthorizationResponse(HttpServletRequest request,
         HttpServletResponse response, Authentication authentication)
         throws IOException {
      this.redirectStrategy.sendRedirect(request, response, "/home");
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
