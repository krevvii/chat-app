package pet.pet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pet.pet.model.Message;
import pet.pet.model.User;
import pet.pet.repository.UserRepository;
import pet.pet.security.CustomUserDetails;
import pet.pet.service.MessageService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/rooms")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    public MessageController(MessageService messageService, UserRepository userRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessagesForRoom(@PathVariable Long roomId) {
        List<Message> messages = messageService.getMessagesForRoom(roomId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{roomId}/messages")
    public ResponseEntity<String> postMessage(
            @PathVariable Long roomId,
            @Valid @RequestBody MessageRequest messageRequest,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        try {
            User author = userRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new NoSuchElementException("Пользователь не найден в базе"));

            messageService.saveMessage(messageRequest.getContent(), roomId, author);

            return ResponseEntity.ok("Сообщение успешно отправлено в комнату " + roomId);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body("Доступ запрещен: вы не состоите в этой комнате.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}