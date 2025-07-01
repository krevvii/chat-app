package pet.pet.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastMessageToRoom(Long roomId, Object messagePayload) {
        String destination = "/topic/room/" + roomId;
        messagingTemplate.convertAndSend(destination, messagePayload);
}

}
