package sv.com.udb.components.mail.sender.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@NoArgsConstructor
public class MailProperties {
   @NotNull
   private String remoteAddress;
}
