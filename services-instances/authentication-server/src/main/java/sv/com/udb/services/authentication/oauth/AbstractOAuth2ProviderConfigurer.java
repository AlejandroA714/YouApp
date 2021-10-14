package sv.com.udb.services.authentication.oauth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.util.matcher.RequestMatcher;

@RequiredArgsConstructor
public abstract class AbstractOAuth2ProviderConfigurer {
   @Getter
   private final ObjectPostProcessor<Object> objectPostProcessor;

   protected abstract <B extends HttpSecurityBuilder<B>> void init(B builder);

   protected abstract <B extends HttpSecurityBuilder<B>> void configure(
         B builder);

   protected abstract RequestMatcher getRequestMatcher();

   protected final <T> T postProcess(T object) {
      return (T) this.objectPostProcessor.postProcess(object);
   }
}
