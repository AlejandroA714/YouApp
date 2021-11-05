package sv.com.udb.services.authentication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("sv.com.udb.services")
@EnableJpaRepositories("sv.com.udb.services")
@SpringBootApplication(scanBasePackages = "sv.com")
public class AuthenticationApplication {
   public static void main(String[] args) {
      new SpringApplicationBuilder(AuthenticationApplication.class)
            .registerShutdownHook(true).run(args);
   }
}
