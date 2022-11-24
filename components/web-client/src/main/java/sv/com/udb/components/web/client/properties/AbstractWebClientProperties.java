package sv.com.udb.components.web.client.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public abstract class AbstractWebClientProperties {
   @NotNull
   @URL(message = "Invalid URL")
   private String remoteAddress;
}
