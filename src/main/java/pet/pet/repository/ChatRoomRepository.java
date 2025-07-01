package pet.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pet.pet.model.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}