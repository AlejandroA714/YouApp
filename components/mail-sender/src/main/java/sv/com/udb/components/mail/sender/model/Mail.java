package sv.com.udb.components.mail.sender.model;

import lombok.*;

import java.util.Map;

@Data
@Builder
@ToString
public class Mail {
   private String       from;
   private String       to;
   private String       subject;
   private HtmlTemplate htmlTemplate;

   @Data
   @Builder
   @ToString
   public static class HtmlTemplate {
      private MailType            template;
      private Map<String, Object> props;

      public HtmlTemplate(MailType template, Map<String, Object> props) {
         this.template = template;
         this.props = props;
      }
   }
}