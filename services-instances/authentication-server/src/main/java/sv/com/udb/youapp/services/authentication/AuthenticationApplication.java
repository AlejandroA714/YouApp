package sv.com.udb.youapp.services.authentication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("sv.com.udb.youapp")
@EnableJpaRepositories(basePackages = "sv.com.udb.youapp")
@SpringBootApplication(scanBasePackages = "sv.com")
public class AuthenticationApplication {
   public static void main(String[] args) {
      new SpringApplicationBuilder(AuthenticationApplication.class)
            .registerShutdownHook(true).run(args);
   }
}
