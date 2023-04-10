package com.eventmaster.backend.InformationDistribution.Chat;

import com.eventmaster.backend.EventManagement.Event;
import com.eventmaster.backend.EventManagement.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final EventRepository eventRepository;
    public ChatService(ChatRepository chatRepository, EventRepository eventRepository){
        this.chatRepository = chatRepository;
        this.eventRepository = eventRepository;
    }


    public Chat getChatById(long eventId) {
        return chatRepository.findById(eventId).orElse(null);
    }

    public void createChat(long eventId, Chat chat) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event not found"));
        chat.setEvent(event);
        chatRepository.save(chat);
    }
}
