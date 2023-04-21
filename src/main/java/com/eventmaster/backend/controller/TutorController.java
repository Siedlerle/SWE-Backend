package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.serviceswithouttoken.ChatService;
import com.eventmaster.backend.serviceswithouttoken.DocumentService;
import com.eventmaster.backend.serviceswithouttoken.UserInEventWithRoleService;
import org.springframework.http.ResponseEntity;
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

    public TutorController(UserInEventWithRoleService userInEventWithRoleService,
                           ChatService chatService,
                           DocumentService documentService) {
        this.userInEventWithRoleService = userInEventWithRoleService;
        this.chatService = chatService;
        this.documentService = documentService;
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
                                                                @RequestParam List<Long> userIds,
                                                                @RequestParam List<Boolean> newStates) {
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
     * @param message Text of the chat.
     * @param authToken AuthToken to identify the user.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/chat/add")
    public ResponseEntity<String> sendMessage(@PathVariable long eventId,
                                              @RequestParam String message,
                                              @RequestParam String authToken) {
        long userId = 0;
        return ResponseEntity.ok(chatService.sendMessage(eventId, userId, message));
    }

    /**
     * Endpoint to upload a file to an event.
     * @param eventId ID of the event which will contain the document.
     * @param multipartFile File which will be saved.
     * @param authToken Token to identify user.
     * @return String about success or failure.
     * @throws IOException
     */
    @PostMapping("/event/{eventId}/file/upload")
    public ResponseEntity<String> uploadFile(@PathVariable long eventId,
                                             @RequestParam("file")MultipartFile multipartFile,
                                             @RequestParam String authToken) throws IOException {
        return ResponseEntity.ok(documentService.createDocument(eventId, multipartFile));
    }

    /**
     * Endpoint to delete a token from database and server.
     * @param docId ID of the document.
     * @param authToken Token to identify user.
     * @return String about success or failure.
     */
    @PostMapping("/event/file/{docId}/delete")
    public ResponseEntity<String> deleteFile(@PathVariable long docId,
                                             @RequestParam String authToken) {
        return ResponseEntity.ok(documentService.deleteDocument(docId));
    }
}
