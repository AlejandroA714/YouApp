package sv.com.udb.components.mail.sender.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@NoArgsConstructor
public class MailProperties {
   @NotNull
   private String remoteAddress;
}
