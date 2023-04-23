package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.CommentRepository;
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

    public CommentService (CommentRepository commentRepository){
        this.commentRepository = commentRepository;
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
    public String commentOnChat(long chatId, String text){
        try {
            Comment comment = commentRepository.findCommentByChatId(chatId);
            comment.setText(text);
            commentRepository.save(comment);

            return LocalizedStringVariables.COMMENTSUBMITSUCCESSMESSAGE;
        }catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.COMMENTSUBMITFAILUREMESSAGE;
        }
    }

}
