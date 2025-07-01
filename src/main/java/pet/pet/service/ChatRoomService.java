package pet.pet.service;

import org.springframework.stereotype.Service;
import pet.pet.model.ChatRoom;
import pet.pet.model.User;
import pet.pet.repository.ChatRoomRepository;

import java.util.List;
import java.util.Set;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public List<ChatRoom> findAllRooms() {
    return chatRoomRepository.findAll();
}

    public ChatRoom createRoom(String roomName, User creator) {
        ChatRoom newRoom = new ChatRoom();
        newRoom.setName(roomName);
        newRoom.setMembers(Set.of(creator));
        return chatRoomRepository.save(newRoom);
    }
}