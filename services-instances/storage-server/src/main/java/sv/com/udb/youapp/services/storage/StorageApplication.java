package sv.com.udb.youapp.services.storage;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(scanBasePackages = "sv.com")
public class StorageApplication {
   public static void main(String[] args) {
      new SpringApplicationBuilder(StorageApplication.class)
            .registerShutdownHook(true).run(args);
   }
}
