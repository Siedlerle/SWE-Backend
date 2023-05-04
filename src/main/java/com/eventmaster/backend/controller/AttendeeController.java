package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.services.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * A class which handles the HTTP-requests for attendee functions.
 *
 * @author Fabian Eilber
 */

@RestController
@CrossOrigin
@RequestMapping("/attendee")
public class AttendeeController {

    private final ChatService chatService;
    private final CommentService commentService;
    private final DocumentService documentService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    public AttendeeController(ChatService chatService,
                              CommentService commentService,
                              DocumentService documentService,
                              QuestionService questionService,
                              AnswerService answerService) {
        this.chatService = chatService;
        this.commentService = commentService;
        this.documentService = documentService;
        this.questionService = questionService;
        this.answerService = answerService;
    }


    /**
     * Endpoint for an attendee of an event to get the chat for a certain event
     * @param eventId Id of the corresponding event
     * @return A list of chat objects as they store the message
     */
    @PostMapping("/get-chat/{eventId}")
    public ResponseEntity<List<Chat>> getChatForEvent(@PathVariable Long eventId){
        return ResponseEntity.ok(chatService.getChatForEvent(eventId));
    }

    /**
     * Endpoint for an attendee of an event to get the documents to the corresponding event
     * @param eventId Id of the corresponding event
     * @return A list of document objects as they store the data
     */
    @PostMapping("/get-files/{eventId}")
    public ResponseEntity<List<Document>> getFilesForEvent(@PathVariable Long eventId){
        return ResponseEntity.ok(documentService.getFilesForEvent(eventId));
    }

    /**
     * Endpoint for an attendee of an event to download the files to a corresponding event
     * @param uri Part of the filepath to locate the file
     * @param filename Name of the file that the user want´s to download
     * @return A resource is returned which contains the file the user want´s to download
     * @throws IOException
     */
    @PostMapping("/download-files")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam String uri,
            @RequestParam String filename)
            throws IOException {
        return documentService.downloadFile(uri, filename);
    }

    /**
     * Endpoint for an attendee of an event to get the survey
     * @param eventId Id of the corresponding event
     * @param emailAdress EMail of the attendee
     * @return List of question objects as they store the questions
     */
    @PostMapping("/get-survey/{eventId}/{emailAdress}")
    public ResponseEntity<List<Question>> getSurveyForEvent(
            @PathVariable long eventId,
            @PathVariable String emailAdress) {
        return ResponseEntity.ok(questionService.getUnfinishedQuestionsByUser(eventId, emailAdress));
    }

    /**
     * Endpoint for an attendee of an event to answer the survey
     * @param answers List of answer objects which store the answers
     * @param emailAdress EMail of the corresponding user
     * @return successmessage
     */
    @PostMapping("/answer-survey/{emailAdress}")
    public ResponseEntity<?> answerSurvey(
            @RequestBody List<Answer> answers,
            @PathVariable String emailAdress){
        return ResponseEntity.ok(answerService.addAnswer(answers, emailAdress));
    }

    /**
     * Endpoint for an attendee of an event to get comments on a chat
     * @param chatId Id of the corresponding chat
     * @param userId Id of the corresponding user
     * @return List of comment objects as they store the comments
     */
    @PostMapping("/get-comment-on-chat/{chatId}/{userId}")
    public ResponseEntity<List<Comment>> getCommentsForChat(
            @PathVariable long chatId,
            @PathVariable long userId){
        return ResponseEntity.ok(commentService.getCommentsForChat(chatId));
    }

    /**
     * Endpoint for an attendee to comment on a chat
     * @param chatId Id of the corresponding chat
     * @param text The text of the comment
     * @return successmessage
     */
    @PostMapping("/comment-on-chat/{chatId}")
    public ResponseEntity<String> commentOnChat(
            @PathVariable Long chatId,
            @RequestParam String text){
        return ResponseEntity.ok(commentService.commentOnChat(chatId, text));
    }

}
