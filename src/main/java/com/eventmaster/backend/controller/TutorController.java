package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Answer;
import com.eventmaster.backend.entities.MessageResponse;
import com.eventmaster.backend.entities.Question;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.services.*;
import org.aspectj.bridge.Message;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * A class which handles the HTTP-requests for tutor functions.
 *
 * @author Fabian Unger
 */

@RestController
@CrossOrigin
@RequestMapping("/tutor")
public class TutorController {

    private final UserInEventWithRoleService userInEventWithRoleService;
    private final ChatService chatService;
    private final DocumentService documentService;
    private final QuestionService questionService;

    public TutorController(UserInEventWithRoleService userInEventWithRoleService,
                           ChatService chatService,
                           DocumentService documentService,
                           QuestionService questionService) {
        this.userInEventWithRoleService = userInEventWithRoleService;
        this.chatService = chatService;
        this.documentService = documentService;
        this.questionService = questionService;
    }

    /**
     * Endpoint to get all attendees of an event.
     * @param eventId ID of the event.
     * @return List of users who attend at the event.
     */
    @PostMapping("/event/{eventId}/attendees/get-all")
    public ResponseEntity<List<User>> getAttendeesForEvent(@PathVariable long eventId) {
        return ResponseEntity.ok(userInEventWithRoleService.getAttendeesForEvent(eventId));
    }

    /**
     * Endpoint to get the attending states for a list of users at an event.
     * @param eventId ID of the event.
     * @param userIds List of userIds.
     * @return List of booleans if user attend or not.
     */
    @PostMapping("/event/{eventId}/attendees/get-status")
    public ResponseEntity<List<Boolean>> getAttendingStatusForUsers(@PathVariable long eventId,
                                                                    @RequestBody List<Long> userIds) {
        return ResponseEntity.ok(userInEventWithRoleService.getAttendingStatusForUsers(eventId, userIds));
    }

    /**
     * Endpoint to update the attending states of the users at an event.
     * @param eventId ID of the event.
     * @param userIds List of ids of users.
     * @param newStates New attending states in same order as userIds List.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/attendees/update-status")
    public ResponseEntity<String> updateAttendingStatusForUsers(@PathVariable long eventId,
                                                                @RequestParam("userIds") List<Long> userIds,
                                                                @RequestParam("attending") List<Boolean> newStates) {
        return ResponseEntity.ok(userInEventWithRoleService.updateAttendingStatusForUsers(eventId, userIds, newStates));
    }

    /**
     * Endpoint to add a user to an event without any invitation.
     * @param eventId Id of the event.
     * @param userMail Mail address of the user.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/attendees/add")
    public ResponseEntity<String> addUserToEvent(@PathVariable long eventId,
                                                 @RequestBody String userMail) {
        return ResponseEntity.ok(userInEventWithRoleService.addUserToEvent(eventId, userMail));
    }

    /**
     * Endpoint to create a Chat and connect it to its event.
     * @param eventId Id of the event which contains the chat.
     * @param emailAdress EMail of the corresponding user
     * @param message Text of the chat.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/chat/add/{emailAdress}")
    public ResponseEntity<MessageResponse> sendMessage(@PathVariable long eventId,
                                                       @PathVariable String emailAdress,
                                                       @RequestBody String message) {
        return ResponseEntity.ok(chatService.sendMessage(eventId,emailAdress,message));
    }

    /**
     * Endpoint to upload a file to an event.
     * @param eventId ID of the event which will contain the document.
     * @param multipartFile File which will be saved.
     * @return String about success or failure.
     * @throws IOException
     */
    @PostMapping("/event/{eventId}/file/upload")
    public ResponseEntity<MessageResponse> uploadFile(@PathVariable long eventId,
                                                      @RequestParam("file")MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(documentService.createDocument(eventId, multipartFile));
    }

    /**
     * Endpoint to delete a token from database and server.
     * @param docId ID of the document.
     * @return String about success or failure.
     */
    @PostMapping("/event/file/{docId}/delete")
    public ResponseEntity<MessageResponse> deleteFile(@PathVariable long docId) {
        return ResponseEntity.ok(documentService.deleteDocument(docId));
    }

    /**
     * Endpoint to delete a token from database and server.
     * @param eventId ID of the event.
     * @param questions Questionnaire to be created.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/question/add")
    public ResponseEntity<MessageResponse> addQuestion(@PathVariable long eventId,
                                                       @RequestBody List<Question> questions){
        return ResponseEntity.ok(questionService.createQuestion(eventId, questions));
    }

    @PostMapping("/event/{eventId}/question-all")
    public ResponseEntity<List<Question>> getAllQuestionsForEvent(@PathVariable long eventId){
        return ResponseEntity.ok(questionService.getAllQuestionsForEvent(eventId));
    }

    @PostMapping("/event/{eventId}/question-answers")
    public ResponseEntity<List<Answer>> getAllAnswers(@PathVariable long eventId){
        return ResponseEntity.ok(questionService.getAnswersToQuestions(eventId));
    }
}
