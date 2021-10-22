package sv.com.udb.components.mail.sender.services.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import sv.com.udb.components.mail.sender.model.Mail;
import sv.com.udb.components.mail.sender.model.MailType;
import sv.com.udb.components.mail.sender.services.IEmailService;

import javax.mail.MessagingException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class DefaultEmailService implements IEmailService {
   @Getter
   @NonNull
   private final JavaMailSender       emailSender;
   @NonNull
   private final SpringTemplateEngine templateEngine;

   @Override
   public void sendMail(MailType modelType, String to,
         Map<String, Object> props) throws MessagingException {
      Mail mail = Mail.builder().to(to).subject(modelType.getSubject())
            .htmlTemplate(Mail.HtmlTemplate.builder().props(props)
                  .template(modelType).build())
            .build();
      sendMail(mail);
   }

   @Override
   public String getTemplate(Mail mail) {
      Context context = new Context();
      context.setVariables(mail.getHtmlTemplate().getProps());
      return templateEngine.process(
            mail.getHtmlTemplate().getTemplate().getFileName(), context);
   }

   @Override
   public Logger getLogger() {
      return LOGGER;
   }

   @Override
   public String processTemplate(String template, Map<String, Object> props) {
      Context context = new Context();
      context.setVariables(props);
      return templateEngine.process(template, context);
   }
}
