package com.eventmaster.backend.services;

import com.eventmaster.backend.EmailService.EmailService;
import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.EventRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A class which receives and processes the requests of the controller concerning the management of events.
 *
 * @author Fabian Eilber
 * @author Fabian Unger
 */

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserInEventWithRoleService userInEventWithRoleService;
    private final DocumentService documentService;
    private final EmailService emailService;

    private final SimpleMailMessage mailMessage = new SimpleMailMessage();


    public EventService(EventRepository eventRepository,
                        @Lazy UserInEventWithRoleService userInEventWithRoleService,
                        DocumentService documentService) {
                        EmailService emailService){
        this.eventRepository = eventRepository;
        this.userInEventWithRoleService = userInEventWithRoleService;
        this.documentService = documentService;
        this.emailService = emailService;
    }

    /**
     * The database is searched for all events
     * @return List of Event Objects
     */
    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }

    /**
     * The database is searched for all events of an organisation
     * @param orgaId Id of the corresponding organisation
     * @return List of events
     */
    public List<Event> getEventsOfOrganisation(long orgaId){
        return eventRepository.findByOrganisationId(orgaId);
    }


    /**
     * The database is searched for events with the corresponding ID
     * @param eventId ID of the event which will be searched.
     * @return Event Object
     */
    public Event getEventById(Long eventId) {
        try {
            return eventRepository.findById(eventId).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * An existing event in the database is being changed.
     * @param event New event with the new data.
     * @return String about success or failure.
     */
    public MessageResponse changeEvent(Event event, MultipartFile image) {
        try {
            Event updatedEvent = eventRepository.findById(event.getId());
            updatedEvent.setName(event.getName());
            updatedEvent.setDescription(event.getDescription());
            updatedEvent.setStartDate(event.getStartDate());
            updatedEvent.setStartTime(event.getStartTime());
            updatedEvent.setEndDate(event.getEndDate());
            updatedEvent.setEndTime(event.getEndTime());
            updatedEvent.setLocation(event.getLocation());
            updatedEvent.setIsPublic(event.getIsPublic());
            updatedEvent.setStatus(event.getStatus());
            updatedEvent.setType(event.getType());
            eventRepository.save(updatedEvent);

            String oldImageLink = updatedEvent.getImage();

            if (image != null) {
                File oldfile = new File("src/main/upload" + oldImageLink);
                if (oldfile.exists()) {
                    oldfile.delete();
                }
                String imageUrl = documentService.saveEventImage(updatedEvent.getId(), image);
                updatedEvent.setImage(imageUrl);
                this.eventRepository.save(updatedEvent);
            }


            return MessageResponse.builder()
                    .message(LocalizedStringVariables.EVENTCHANGEDSUCCESSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.EVENTCHANGEDFAILUREMESSAGE)
                    .build();
        }
    }
    /**
     * Changing the status of an event.
     * @param eventId ID of the event from which the status will be changed.
     * @param newStatus New status of the event.
     * @return String about success or failure.
     */
    public MessageResponse changeStatusOfEvent(long eventId, String newStatus) {
        try {
            Event event = getEventById(eventId);
            for (EnumEventStatus status : EnumEventStatus.values()) {
                if (status.status.toUpperCase().equals(newStatus)) {
                    event.setStatus(status);

                    this.eventRepository.save(event);

                    return MessageResponse.builder()
                            .message(LocalizedStringVariables.EVENTSTATUSCHANGEDSUCCESSMESSAGE)
                            .build();
                }
            }
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.GIVENEVENTSTATUSNOTCORRECTMESSAGE)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.EVENTSTATUSCHANGEDFAILURESMESSAGE)
                    .build();
        }
    }

    /**
     * An event corresponding to the eventId is being deleted
     * @param eventId ID of the event which will be deleted.
     * @return Boolen as status for success
     */
    public MessageResponse deleteEvent(Long eventId){
        Event event = getEventById(eventId);
        EnumEventStatus status = event.getStatus();
        if (status.equals(EnumEventStatus.CANCELLED)) {
            try {

                List<UserInEventWithRole> userInEventWithRoles = userInEventWithRoleService.getUsersInEvent(eventId);
                for (UserInEventWithRole userInEventWithRole: userInEventWithRoles) {
                    userInEventWithRoleService.deleteUserInEventWithRole(userInEventWithRole);
                }
                eventRepository.deleteById(eventId);
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.EVENTDELETEDSUCCESSMESSAGE)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.EVENTDELETEDFAILUREMESSAGE)
                        .build();
            }
        } else {
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.EVENTSTATUSCHANGEDFAILURESMESSAGE)
                    .build();
        }
    }

    /**
     * An event corresponding to the eventId is being searched to return its participants
     * @param eventId ID of the event which will be updated.
     * @return List of participants
     */
    public List<User> getParticipants(Long eventId){
        //Todo Implemetieren, dass entsprechende User gefunden werden und zurückgegeben werden
        List<User> participants = null;
        return participants;
    }


    /**
     * Sets the status of the event to cancelled and sends the attendees and invitees a mail.
     * @param eventId ID of the event which will be cancelled.
     * @param reason Reason why the event will be cancelled.
     * @return String about success or failure.
     */
    public MessageResponse cancelEvent(long eventId, String reason) {
        try {
            Event event = getEventById(eventId);
            event.setStatus(EnumEventStatus.CANCELLED);
            eventRepository.save(event);

            List<User> attendees = userInEventWithRoleService.getAttendeesForEvent(eventId);

            for (User user:attendees) {
                mailMessage.setFrom("ftb-solutions@outlook.de");
                mailMessage.setTo(user.getEmailAdress());
                mailMessage.setSubject("Eventabsage - "+event.getName());
                mailMessage.setText("Hallo " + user.getFirstname() + ","
                                +"\ndas Event: "+event.getName()
                                +"\nwelches am "+event.getStartDate()+" stattfinden sollte,"
                                +"\nwurde abgesagt. Grund hierfür ist:"
                                +"\n"+reason);
                //emailService.sendEmail(mailMessage);
                System.out.println(mailMessage.getText());
            }

            return MessageResponse.builder()
                    .message(LocalizedStringVariables.EVENTCANCELLEDSUCCESSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.EVENTCANCELLEDFAILUREMESSAGE)
                    .build();
        }
    }

    protected void saveEvent(Event event) {
        eventRepository.save(event);
    }
}
