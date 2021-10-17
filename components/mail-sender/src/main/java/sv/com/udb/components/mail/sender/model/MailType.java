package sv.com.udb.components.mail.sender.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailType {
   CONFIRM_MAIL("confirm_mail", "You App Confirmation"), RECOVER_PASWORD(
         "recovery_mail", "Recover Password");

   private final String fileName;
   private final String subject;
}
