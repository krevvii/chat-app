package pet.pet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pet.pet.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class PetApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetApplication.class, args);
    }
}
