package pet.pet.controller;

import jakarta.validation.constraints.NotBlank;

public class MessageRequest {

    @NotBlank(message = "Содержимое сообщения не может быть пустым")
    private String content;
    
    public MessageRequest(){

    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }
}
