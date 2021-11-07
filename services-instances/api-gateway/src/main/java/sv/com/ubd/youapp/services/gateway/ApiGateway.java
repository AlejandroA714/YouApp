package sv.com.ubd.youapp.services.gateway;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = "sv.com")
public class ApiGateway {
   public static void main(String[] args) {
      new SpringApplicationBuilder(ApiGateway.class).registerShutdownHook(true)
            .run(args);
   }
}
