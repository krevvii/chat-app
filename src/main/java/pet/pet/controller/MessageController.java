package pet.pet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pet.pet.controller.MessageRequest;
import pet.pet.model.Message;
import pet.pet.model.User;
import pet.pet.repository.UserRepository;
import pet.pet.security.CustomUserDetails;
import pet.pet.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    public MessageController(MessageService messageService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @PostMapping
    public ResponseEntity<String> postMessage(
        @RequestBody MessageRequest messageRequest,
        @AuthenticationPrincipal CustomUserDetails currentUser) {

        if (currentUser == null) {
            return ResponseEntity.status(401).body("Пожалуйста, войдите в систему");
        }

        User author = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден в базе"));

        messageService.saveMessage(messageRequest.getContent(), author);

        return ResponseEntity.ok("Сообщение успешно отправлено");
    }
}
