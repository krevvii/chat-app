package pet.pet;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pet.pet.kafka.KafkaConsumer;
import pet.pet.kafka.KafkaProducer;

@SpringBootTest
class PetApplicationTests {

    // Вложенный класс для тестов
    @TestConfiguration
    static class TestConfig {

        // мок для KafkaProducer
        @Bean
        public KafkaProducer kafkaProducer() {
            return Mockito.mock(KafkaProducer.class);
        }

        // мок для KafkaConsumer
        @Bean
        public KafkaConsumer kafkaConsumer() {
            return Mockito.mock(KafkaConsumer.class);
        }
    }

    @Test
    void contextLoads() {
    }

}
