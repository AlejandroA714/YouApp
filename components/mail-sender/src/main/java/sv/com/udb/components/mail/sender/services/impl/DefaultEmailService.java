package sv.com.udb.components.mail.sender.services.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import sv.com.udb.components.mail.sender.model.Mail;
import sv.com.udb.components.mail.sender.services.IEmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class DefaultEmailService implements IEmailService {
   @NonNull
   private final JavaMailSender       emailSender;
   @NonNull
   private final SpringTemplateEngine templateEngine;

   @Override
   public void sendEmail(Mail mail) throws MessagingException {
      LOGGER.info("Trying to send mail: {}", mail);
      MimeMessage message = emailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name());
      String html = getTemplate(mail);
      helper.setTo(mail.getTo());
      helper.setFrom(mail.getFrom());
      helper.setSubject(mail.getSubject());
      helper.setText(html, true);
      emailSender.send(message);
      LOGGER.info("email to: {} has been send", mail.getTo());
   }

   private String getTemplate(Mail mail) {
      Context context = new Context();
      context.setVariables(mail.getHtmlTemplate().getProps());
      return templateEngine
            .process(mail.getHtmlTemplate().getTemplate().getLabel(), context);
   }
}
