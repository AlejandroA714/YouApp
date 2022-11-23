package sv.com.udb.youapp.services.authentication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = "sv.com")
public class AuthenticationApplication {
   public static void main(String[] args) {
      new SpringApplicationBuilder(AuthenticationApplication.class)
            .registerShutdownHook(true).run(args);
   }
}
