package sv.com.udb.youapp.services.storage;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("sv.com.udb.services")
@EnableJpaRepositories("sv.com.udb.services")
@SpringBootApplication(scanBasePackages = "sv.com")
public class StorageApplication {
   public static void main(String[] args) {
      new SpringApplicationBuilder(StorageApplication.class)
            .registerShutdownHook(true).run(args);
   }
}
