package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.Chat;
import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.repositories.ChatRepository;
import com.eventmaster.backend.repositories.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of chats
 *
 * @author Fabian Eilber
 */

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final EventRepository eventRepository; //TODO: Stattdessen auf EventService zugreifen.
    public ChatService(ChatRepository chatRepository, EventRepository eventRepository){
        this.chatRepository = chatRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Searching for the Chat corresponding to an event
     * @param eventId Id of the event the chat is searched for
     * @return A list of chat objects as they store the message
     */
    public List<Chat> getChatForEvent(long eventId) {
        return chatRepository.findChatById(eventId);
    }

    public void createChat(long eventId, Chat chat) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event not found"));
        chat.setEvent(event);
        chatRepository.save(chat);
    }
}
