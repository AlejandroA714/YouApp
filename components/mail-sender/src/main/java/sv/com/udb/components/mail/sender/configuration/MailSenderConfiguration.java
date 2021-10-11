package sv.com.udb.components.mail.sender.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import sv.com.udb.components.mail.sender.properties.MailProperties;

import java.util.Properties;

@Configuration
public class MailSenderConfiguration {
   @Bean
   @ConfigurationProperties("dd")
   public MailProperties mailProperties() {
      return new MailProperties();
   }

   @Bean
   public JavaMailSender getJavaMailSender(MailProperties mailProperties) {
      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
      mailSender.setHost(mailProperties.getHost());
      mailSender.setPort(mailProperties.getPort());
      mailSender.setUsername(mailProperties.getUsername());
      mailSender.setPassword(mailProperties.getPassword());
      Properties props = mailSender.getJavaMailProperties();
      props.put("mail.transport.protocol", mailProperties.getProtocol());
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable",
            mailProperties.getEnableSSL().toString());
      props.put("mail.debug", "true");
      return mailSender;
   }
}
