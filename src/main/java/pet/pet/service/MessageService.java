package pet.pet.service;

import org.springframework.stereotype.Service;
import pet.pet.model.Message;
import pet.pet.model.User;
import pet.pet.repository.MessageRepository;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public void saveMessage(String content, User author) {
        Message message = new Message();
        message.setContent(content);
        message.setAuthor(author);
        messageRepository.save(message);
    }
}