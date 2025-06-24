package pet.pet.model;

import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    public Message() {
    }

    public Message(String content) {
        this.content = content;
    }

    public User getAuthor(){
        return author;
    }
    public void setAuthor(User author){
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
