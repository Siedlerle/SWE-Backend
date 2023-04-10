package com.eventmaster.backend.InformationDistribution.Chat;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    /**
     *Database is searched for chats corresponding to an event
     * @param eventId Id of the event the chat is used for
     * @return Chat which is being found
     */
    @PostMapping("/get-chat/{eventId}")
    public ResponseEntity<Chat> getChat(@PathVariable long eventId){
        return ResponseEntity.ok(chatService.getChatById(eventId));
    }

    /**
     * Creates a new post in the chat
     * @param eventId Id of the event the chat is used for
     * @param chat The post which is being added
     */
    @PostMapping("/add-chat/{eventId}")
    public void addChat(@PathVariable long eventId, @RequestBody Chat chat) {
        chatService.createChat(eventId, chat);
    }

}
