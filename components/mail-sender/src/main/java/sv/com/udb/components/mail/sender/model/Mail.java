package sv.com.udb.components.mail.sender.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
      private ModelType           template;
      private Map<String, Object> props;

      public HtmlTemplate(ModelType template, Map<String, Object> props) {
         this.template = template;
         this.props = props;
      }
   }
}