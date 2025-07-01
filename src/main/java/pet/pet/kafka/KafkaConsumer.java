package pet.pet.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pet.pet.controller.ChatMessagePayload;
import pet.pet.websocket.WebSocketController;

@Component
public class KafkaConsumer {

    private final WebSocketController webSocketController;
    private final ObjectMapper objectMapper;

    public KafkaConsumer(WebSocketController webSocketController, ObjectMapper objectMapper) {
        this.webSocketController = webSocketController;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${kafka.topic.main}", groupId = "my-group")
    public void listen(String jsonMessage) {
        System.out.println("📥 Получено JSON-сообщение от Kafka: " + jsonMessage);
        try {
            ChatMessagePayload payload = objectMapper.readValue(jsonMessage, ChatMessagePayload.class);
            webSocketController.broadcastMessageToRoom(payload.getRoomId(), payload);
        } catch (JsonProcessingException e) {
            System.err.println("Ошибка при десериализации сообщения из JSON: " + e.getMessage());
        }
    }
}