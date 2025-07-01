package pet.pet.controller;

public class ChatMessagePayload {
    private String content;
    private String authorUsername;
    private Long roomId;

    public ChatMessagePayload() {
    }

    public ChatMessagePayload(String content, String authorUsername, Long roomId) {
        this.content = content;
        this.authorUsername = authorUsername;
        this.roomId = roomId;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
}