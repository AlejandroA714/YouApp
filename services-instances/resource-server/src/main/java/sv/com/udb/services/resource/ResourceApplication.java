package sv.com.udb.services.resource;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = "sv.com")
public class ResourceApplication {
   public static void main(String[] args) {
      new SpringApplicationBuilder(ResourceApplication.class)
            .registerShutdownHook(true).run(args);
   }
}
