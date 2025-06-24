package pet.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pet.pet.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
