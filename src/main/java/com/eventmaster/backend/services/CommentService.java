package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.ChatRepository;
import com.eventmaster.backend.repositories.CommentRepository;
import com.eventmaster.backend.repositories.UserRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of comments
 *
 * @author Fabian Eilber
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public CommentService (CommentRepository commentRepository, ChatRepository chatRepository, UserRepository userRepository){
        this.commentRepository = commentRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    /**
     * Searching for comments to a corresponding chat
     * @param chatId Id of the chat the comments are searched for
     * @return List of comment objects as they store the text
     */
    public List<Comment> getCommentsForChat(long chatId){
        return commentRepository.findByChatId(chatId);
    }

    /**
     * Searching for a chat to comment on
     * @param chatId Id of the corresponding chat
     * @param text Text of the comment
     * @return successmessage
     */
    public MessageResponse commentOnChat(long chatId, String text, String emailAdress){
        try {
            //Comment comment = commentRepository.findCommentByChatId(chatId);
            Comment comment = new Comment();
            User user = userRepository.findByEmailAdress(emailAdress);
            Chat chat = chatRepository.getReferenceById(chatId);

            comment.setText(text);
            comment.setChat(chat);
            comment.setUser(user);
            commentRepository.save(comment);

            return MessageResponse.builder().message(LocalizedStringVariables.COMMENTSUBMITSUCCESSMESSAGE).build();
        }catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder().message(LocalizedStringVariables.COMMENTSUBMITFAILUREMESSAGE).build();
        }
    }

}
