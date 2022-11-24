package sv.com.udb.components.mail.sender.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import sv.com.udb.components.mail.sender.model.Mail;
import sv.com.udb.components.mail.sender.model.MailType;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public interface IEmailService {
   JavaMailSender getEmailSender();

   String getTemplate(Mail mail);

   Logger getLogger();

   String processTemplate(String template, Map<String, Object> props);

   void sendMail(MailType modelType, String to, Map<String, Object> props)
         throws MessagingException;

   default void sendMail(Mail mail) throws MessagingException {
      getLogger().info("Trying to send mail: {}", mail);
      MimeMessage message = getEmailSender().createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name());
      String html = getTemplate(mail);
      helper.setTo(mail.getTo());
      helper.setSubject(mail.getSubject());
      helper.setText(html, true);
      getEmailSender().send(message);
      getLogger().info("email to: {} has been send type: {}", mail.getTo(),
            mail.getHtmlTemplate().getTemplate().name());
   }
}
