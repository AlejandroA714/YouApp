package sv.com.udb.components.mail.sender.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;
import sv.com.udb.components.mail.sender.model.Mail;
import sv.com.udb.components.mail.sender.properties.MailProperties;
import sv.com.udb.components.mail.sender.services.IEmailService;
import sv.com.udb.components.mail.sender.services.impl.DefaultEmailService;

@Configuration
public class MailSenderConfiguration {
   @Bean
   @ConfigurationProperties(prefix = "app.mail")
   public MailProperties mailProperties() {
      return new MailProperties();
   }

   @Bean
   public IEmailService emailService(JavaMailSender mailSender,
         SpringTemplateEngine templateEngine, MailProperties mailProperties) {
      return new DefaultEmailService(mailSender, templateEngine,
            mailProperties);
   }
}
