package pet.pet.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pet.pet.model.Message;
import pet.pet.repository.MessageRepository;
import pet.pet.websocket.WebSocketController;

@Component
public class KafkaConsumer {

    private final MessageRepository messageRepository;
    private final WebSocketController webSocketController;

    public KafkaConsumer(MessageRepository messageRepository, WebSocketController webSocketController) {
        this.messageRepository = messageRepository;
        this.webSocketController = webSocketController;
    }

    @KafkaListener(topics = "${kafka.topic.main}", groupId = "my-group")
    public void listen(String message) {
        System.out.println(" Получено сообщение от Kafka: " + message);
        messageRepository.save(new Message(message));
        webSocketController.sendMessage(message);
    }
}
