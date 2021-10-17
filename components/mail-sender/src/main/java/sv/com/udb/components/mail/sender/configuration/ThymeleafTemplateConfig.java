package sv.com.udb.components.mail.sender.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;

@Configuration
public class ThymeleafTemplateConfig {
   @Bean
   public SpringTemplateEngine springTemplateEngine(
         ClassLoaderTemplateResolver templateResolver) {
      SpringTemplateEngine templateEngine = new SpringTemplateEngine();
      templateEngine.addTemplateResolver(templateResolver);
      return templateEngine;
   }

   @Bean
   public ClassLoaderTemplateResolver htmlTemplateResolver() {
      ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
      emailTemplateResolver.setPrefix("/templates/");
      emailTemplateResolver.setSuffix(".html");
      emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
      emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
      return emailTemplateResolver;
   }
}
