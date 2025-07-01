package pet.pet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.pet.model.ChatRoom;
import pet.pet.model.User;
import pet.pet.repository.UserRepository;
import pet.pet.security.CustomUserDetails;
import pet.pet.service.ChatRoomService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;

    public ChatRoomController(ChatRoomService chatRoomService, UserRepository userRepository) {
        this.chatRoomService = chatRoomService;
        this.userRepository = userRepository;
    }

    @GetMapping
        public ResponseEntity<List<ChatRoom>> getAllRooms() {
        return ResponseEntity.ok(chatRoomService.findAllRooms());
    }

    @PostMapping
    public ResponseEntity<?> createNewRoom(
            @RequestBody CreateRoomRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        User creator = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));

        ChatRoom createdRoom = chatRoomService.createRoom(request.getName(), creator);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            "Комната создана с ID: " + createdRoom.getId()
        );
    }
}