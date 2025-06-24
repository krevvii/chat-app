package pet.pet.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


@Service
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.main}")
    private String topicName;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message){
        kafkaTemplate.send(topicName, message);
        System.out.println("Message sent to Kafka: " + message);
    }
}
