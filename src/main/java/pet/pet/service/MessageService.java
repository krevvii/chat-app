package pet.pet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pet.pet.controller.ChatMessagePayload;
import pet.pet.kafka.KafkaProducer;
import pet.pet.model.ChatRoom;
import pet.pet.model.Message;
import pet.pet.model.User;
import pet.pet.repository.ChatRoomRepository;
import pet.pet.repository.MessageRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    public MessageService(MessageRepository messageRepository, ChatRoomRepository chatRoomRepository, KafkaProducer kafkaProducer, ObjectMapper objectMapper) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    public List<Message> getMessagesForRoom(Long roomId) {
        return messageRepository.findAll().stream()
                .filter(msg -> msg.getChatRoom().getId().equals(roomId))
                .toList();
    }

    public void saveMessage(String content, Long roomId, User author) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException("Комната с ID " + roomId + " не найдена"));

        if (!room.getMembers().contains(author)) {
            throw new AccessDeniedException("Пользователь не является участником комнаты");
        }

        Message message = new Message();
        message.setContent(content);
        message.setAuthor(author);
        message.setChatRoom(room);
        messageRepository.save(message);

        ChatMessagePayload payload = new ChatMessagePayload(
                message.getContent(),
                message.getAuthor().getUsername(),
                message.getChatRoom().getId()
        );

        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);
            kafkaProducer.sendMessage(jsonPayload);
        } catch (JsonProcessingException e) {
            System.err.println("Ошибка при сериализации сообщения в JSON: " + e.getMessage());
        }
    }
}