package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.Chat;
import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.MessageResponse;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.repositories.ChatRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of chats
 *
 * @author Fabian Eilber
 * @author Fabian Unger
 * @version 2.0
 * @since 1.0
 */

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final EventService eventService;
    private final UserService userService;
    public ChatService(ChatRepository chatRepository, EventService eventService, UserService userService){
        this.chatRepository = chatRepository;
        this.eventService = eventService;
        this.userService = userService;
    }

    /**
     * Searching for the Chat corresponding to an event
     * @param eventId Id of the event the chat is searched for
     * @return A list of chat objects as they store the message
     */
    public List<Chat> getChatForEvent(long eventId) {
        try {
            return chatRepository.findChatByEvent_Id(eventId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a chat with the text and connects it to the user and the event.
     * @param eventId ID of the event which contains the chat.
     * @param emailAdress of the user who sent the chat.
     * @param message Message of the chat.
     * @return String about success or failure.
     */
    public MessageResponse sendMessage(long eventId, String emailAdress, String message) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserByMail(emailAdress);

        Chat chat = new Chat();
        chat.setEvent(event);
        chat.setUser(user);
        chat.setMessage(message);
        try {
            chatRepository.save(chat);
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.CHATSUBMITSUCCESSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.CHATSUBMITFAILUREMESSAGE)
                    .build();
        }
    }
}
