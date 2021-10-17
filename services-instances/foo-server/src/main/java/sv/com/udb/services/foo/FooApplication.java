package sv.com.udb.services.foo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = "sv.com")
public class FooApplication {
   public static void main(String[] args) {
      new SpringApplicationBuilder(FooApplication.class)
            .registerShutdownHook(true).run(args);
   }
}
