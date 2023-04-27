package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.Chat;
import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.repositories.EventRepository;
import com.eventmaster.backend.repositories.ChatRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

public class ChatService {

    private final ChatRepository chatRepository;
    private final EventRepository eventRepository; //TODO: Stattdessen auf EventService zugreifen.
    public ChatService(ChatRepository chatRepository, EventRepository eventRepository){
        this.chatRepository = chatRepository;
        this.eventRepository = eventRepository;
    }


    public Chat getChatById(long eventId) {
        return chatRepository.findById(eventId).orElse(null);
    }

    public void createChat(long eventId, Chat chat) {
        Event event = eventRepository.findById(eventId);
        chat.setEvent(event);
        chatRepository.save(chat);
    }
}
