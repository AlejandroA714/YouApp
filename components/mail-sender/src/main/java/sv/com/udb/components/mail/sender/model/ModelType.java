package sv.com.udb.components.mail.sender.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ModelType {
   CONFIRM_MAIL("confirm_mail");

   private final String label;
}
