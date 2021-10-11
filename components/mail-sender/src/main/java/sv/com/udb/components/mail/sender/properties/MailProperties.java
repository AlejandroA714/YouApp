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
   private String  host;
   @NotNull
   private Integer port;
   @NotNull
   private String  username;
   @NotNull
   private String  password;
   @NotNull
   private Boolean enableSSL;
   @NotNull
   private String  protocol;
}
