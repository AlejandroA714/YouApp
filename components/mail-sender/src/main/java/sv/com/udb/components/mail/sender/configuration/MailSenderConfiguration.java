package sv.com.udb.components.mail.sender.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;
import sv.com.udb.components.mail.sender.services.IEmailService;
import sv.com.udb.components.mail.sender.services.impl.DefaultEmailService;

@Configuration
public class MailSenderConfiguration {
   @Bean
   public IEmailService emailService(JavaMailSender mailSender,
         SpringTemplateEngine templateEngine) {
      return new DefaultEmailService(mailSender, templateEngine);
   }
}
