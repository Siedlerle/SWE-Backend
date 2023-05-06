package com.eventmaster.backend.services;

import com.eventmaster.backend.EmailService.EmailService;
import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.UserInEventWithRoleRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.events.EventTarget;

import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of events
 *
 * @author Fabian Eilber
 * @author Fabian Unger
 */

@Service
public class UserInEventWithRoleService {

    private final UserInEventWithRoleRepository userInEventWithRoleRepository;
    private final UserService userService;
    private final EventService eventService;
    private final OrganisationService organisationService;
    private final EventSeriesService eventSeriesService;
    private final EventRoleService eventRoleService;
    private final UserInOrgaWithRoleService userInOrgaWithRoleService;
    private final DocumentService documentService;
    private final EmailService emailService;


    private final SimpleMailMessage mailMessage = new SimpleMailMessage();

    public UserInEventWithRoleService(
            UserInEventWithRoleRepository userInEventWithRoleRepository,
            UserService userService,
            EventService eventService,
            OrganisationService organisationService,
            EventSeriesService eventSeriesService,
            EventRoleService eventRoleService,
            DocumentService documentService,
            @Lazy UserInOrgaWithRoleService userInOrgaWithRoleService,
            EmailService emailService) {
        this.userInEventWithRoleRepository = userInEventWithRoleRepository;
        this.eventService = eventService;
        this.userService = userService;
        this.organisationService = organisationService;
        this.eventSeriesService = eventSeriesService;
        this.eventRoleService = eventRoleService;
        this.userInOrgaWithRoleService = userInOrgaWithRoleService;
        this.documentService = documentService;
        this.emailService = emailService;
    }


    //region UserMethods

    /**
     * A user is being registered for an event
     * @param eventId Id of the corresponding event
     * @param emailAddress Email of the corresponding user
     * @return success message
     */
    public MessageResponse registerForEvent(long eventId, String emailAddress){
        User user = userService.getUserByMail(emailAddress);
        Event event = eventService.getEventById(eventId);
        if(userInEventWithRoleRepository.existsByUserAndEvent(user, event)) {
            UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(user, event);
            EventRole attendeeRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);
            EventRole groupAttendeeRole = eventRoleService.findByRole(EnumEventRole.GROUPATTENDEE);
            if (userInEventWithRole.getEventRole().equals(attendeeRole) || userInEventWithRole.getEventRole().equals(groupAttendeeRole)) {
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.USERALREADYATTENDTINGTOEVENT)
                        .build();
            } else {
                userInEventWithRole.setEventRole(attendeeRole);
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.USERREGISTERESFOREVENTSUCCESSMESSAGE)
                        .build();
            }
        } else {
            try {

                EventRole eventRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);

                UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
                userInEventWithRole.setEvent(eventService.getEventById(eventId));
                userInEventWithRole.setUser(userService.getUserByMail(emailAddress));
                userInEventWithRole.setEventRole(eventRole);
                userInEventWithRoleRepository.save(userInEventWithRole);

                mailMessage.setFrom("ftb-solutions@outlook.de");
                mailMessage.setTo(user.getEmailAdress());
                mailMessage.setSubject("Eventregistrierung - "+event.getName());
                mailMessage.setText("Hallo " + user.getFirstname() + "," +
                        "\nDu hast dich zu folgendem Event angemeldet"
                        +"\nName: "+event.getName()
                        +"\nOrt: "+event.getLocation()
                        +"\nBegin: "+event.getStartDate()
                        +"\nBeschreibung: "+event.getDescription()
                        );
                //emailService.sendEmail(mailMessage);
                System.out.println(mailMessage);
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.USERREGISTERESFOREVENTSUCCESSMESSAGE)
                        .build();
            }catch (Exception e) {
                e.printStackTrace();
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.USERREGISTERESFOREVENTFAILUREMESSAGE)
                        .build();
            }
        }
    }

    /**
     * A user can accept an invitation to an event
     * @param eventId Id of the event the user is invited to
     * @param emailAdress Email of the user who is being invited
     * @return success message
     */
    public MessageResponse acceptEventInvitation(long eventId, String emailAdress){
        try {
            User user = userService.getUserByMail(emailAdress);
            UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUser_IdAndEvent_Id(user.getId(), eventId);

            EnumEventRole userRole = userInEventWithRole.getEventRole().getRole();

            EventRole eventRole = new EventRole();

            switch (userRole) {
                case INVITED:
                    eventRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);
                    break;
                case SERIESINVITED:
                    eventRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);
                    break;
                case TUTORINVITED:
                    eventRole = eventRoleService.findByRole(EnumEventRole.TUTOR);
                    break;
                case TUTORSERIESINVITED:
                    eventRole = eventRoleService.findByRole(EnumEventRole.TUTOR);
                    break;
                case GROUPINVITED:
                    eventRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);
                    break;
                case GROUPSERIESINVITED:
                    eventRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);
                    break;
                default:
                    eventRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);
                    break;
            }


            userInEventWithRole.setEvent(eventService.getEventById(eventId));
            userInEventWithRole.setUser(userService.getUserByMail(emailAdress));
            userInEventWithRole.setEventRole(eventRole);
            userInEventWithRoleRepository.save(userInEventWithRole);

            return MessageResponse.builder()
                    .message(LocalizedStringVariables.EVENTINVITATIONACCEPTEDSUCCESSMESSAGE)
                    .build();
        }catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.EVENTINVITATIONACCEPTEDFAILUREMESSAGE)
                    .build();
        }
    }

    /**
     * A user can decline an invitation to an event
     * @param eventId Id of the event the user is invited to
     * @param emailAdress Email of the user who is being invited
     * @return success message
     */
    public MessageResponse declineEventInvitation(long eventId, String emailAdress){
        try {
            User user = userService.getUserByMail(emailAdress);
            Event event = eventService.getEventById(eventId);

            UserInEventWithRole userInEventWithRole= userInEventWithRoleRepository.findByUserAndEvent(user, event);

            if(userInEventWithRole!=null) {
                userInEventWithRoleRepository.delete(userInEventWithRole);
            }
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.EVENTINVITATIONDECLINEDSUCCESSMESSAGE)
                    .build();
        }catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.EVENTINVITATIONDECLINEDFAILUREMESSAGE)
                    .build();
        }
    }

    /**
     * For authorization purposes u can get your role corresponding to an event
     * @param eventId Id of the event the user is requesting his role for
     * @param emailAdress Email of the user who is requesting the role
     * @return Role of the user
     */
    public EventRole getRoleForEvent(long eventId, String emailAdress){
        try {

            User user = userService.getUserByMail(emailAdress);
            Event event = eventService.getEventById(eventId);
            UserInEventWithRole userInEvent = userInEventWithRoleRepository.findByUserAndEvent(user, event);

            return userInEvent.getEventRole();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the organizer of an event.
     * @param eventId ID of the event.
     * @return User who is organizer of the event.
     */
    public User getOrganizerOfEvent(long eventId) {
        Event event = eventService.getEventById(eventId);
        EventRole organizerRole = eventRoleService.findByRole(EnumEventRole.ORGANIZER);

        UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByEventAndEventRole(event, organizerRole);

        User organizer = userInEventWithRole.getUser();

        return organizer;
    }

    /**
     * Retrieving all events where user is not affiliated in his organisations
     * @param emailAddress Email of the corresponding user
     * @return List of Events
     */
    public List<Event> getAllAvailableEventsForUser(String emailAddress){
        User user = userService.getUserByMail(emailAddress);
        List<Event> userEvents = userInEventWithRoleRepository.
                findByUser_Id(user.getId())
                .stream()
                .map(UserInEventWithRole::getEvent)
                .toList();
        List<Organisation> organisation = userInOrgaWithRoleService.getOrgasForUser(user.getEmailAdress());
        return organisation
                .stream()
                .map(orga->eventService.getEventsOfOrganisation(orga.getId()))
                .flatMap(List::stream)
                .filter(Event::getIsPublic)
                .filter(event->!userEvents.contains(event))
                .toList();
    }


    /**
     * A user can get all events he is registered for
     * @param emailAdress Email of the user to get registered events for
     * @return List of events the user is registered for
     */
    public List<Event> getRegisteredEventsForUser(String emailAdress){
        try {
            User user = userService.getUserByMail(emailAdress);

            List<UserInEventWithRole> userInEvents = userInEventWithRoleRepository.findByUser_Id(user.getId());

            List<Event> userEvents = new ArrayList<>();

            for (UserInEventWithRole event: userInEvents) {
                if(event != null){
                    if(event.getEventRole().getRole().equals(EnumEventRole.ATTENDEE) || event.getEventRole().getRole().equals(EnumEventRole.GROUPATTENDEE) || event.getEventRole().getRole().equals(EnumEventRole.TUTOR)){
                        userEvents.add(event.getEvent());
                    }
                }
            }


            return userEvents;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A user can unregister from an event
     * @param eventId Corresponding event
     * @param emailAdress Id of the user who is about to unregister
     * @return success message
     */
    public MessageResponse unregisterFromEvent(long eventId,String emailAdress, String reason){
        try {

            User user = userService.getUserByMail(emailAdress);
            Event event = eventService.getEventById(eventId);
            User organizer = new User();
            String reasonInMail = "";

            List<UserInEventWithRole> users = userInEventWithRoleRepository.findByEvent_Id(eventId);
            for (UserInEventWithRole userInEvent :users) {
                if(userInEvent.getEventRole().equals(EnumEventRole.ORGANIZER)){
                    organizer = userInEvent.getUser();
                }
            }

            if(!reason.equals("{}")){
                reasonInMail = "Der Grund für die Absage war:\n"+reason;
            }

            mailMessage.setFrom("ftb-solutions@outlook.de");
            mailMessage.setTo(organizer.getEmailAdress());
            mailMessage.setSubject("Absage von Event - "+event.getName());
            mailMessage.setText("Hallo " + organizer.getFirstname() + ","
                    + "\nder Benutzer " + user.getFirstname() +" hat sich"
                    +"\nvon dem Event "+event.getName()+"abgemeldet."
                    +"\n"+reasonInMail);
            //emailService.sendEmail(mailMessage);
            System.out.println(mailMessage.getText());

            UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(user, event);
            userInEventWithRoleRepository.delete(userInEventWithRole);

            return MessageResponse.builder()
                    .message(LocalizedStringVariables.USERUNREGISTERFROMEVENTSUCCESSMESSAGE)
                    .build();
        }catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.USERUNREGISTERFROMEVENTFAILUREMESSAGE)
                    .build();
        }

    }

    /**
     * Gets all events of a user where he is invited to.
     * @param userMail Mail of the user.
     * @return List of events.
     */
    public List<Event> getEventInvitationsForUser(String userMail) {
        try {
            User user = userService.getUserByMail(userMail);

            List<Event> eventInvitations = new ArrayList<>();

            List<UserInEventWithRole> userInEventWithRoleList = userInEventWithRoleRepository.findByUser_Id(user.getId());
            for (UserInEventWithRole userInEventWithRole : userInEventWithRoleList) {
                Event event = userInEventWithRole.getEvent();
                if (isUserInvitedToEvent(event.getId(), userMail)) {
                    eventInvitations.add(event);
                }
            }
            return eventInvitations;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Retrieves all the users for a specific event
     * @param evenId Id of the corresponding event
     * @return List of UserInEventWithRole
     */
    public List<UserInEventWithRole> getUsersInEvent (long evenId){
        return userInEventWithRoleRepository.findByEvent_Id(evenId);
    }

    /**
     * Retrieves all the Events of a user
     * @param userId Id of the corresponding user
     * @return List of userineventwithrole
     */
    public List<UserInEventWithRole> getEventsForUser(long userId){
        return userInEventWithRoleRepository.findByUser_Id(userId);
    }

    /**
     * Deletes a specific UserInEventWithRole
     * @param userInEventWithRole Object which is being deleted
     */
    public void deleteUserInEventWithRole(UserInEventWithRole userInEventWithRole){
        userInEventWithRoleRepository.delete(userInEventWithRole);
    }

    /**
     * Checks if a user is invited to an event.
     * @param eventId ID of the event.
     * @param userMail Mail of the user.
     * @return Boolean if user is invited or not.
     */
    public Boolean isUserInvitedToEvent(long eventId, String userMail) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserByMail(userMail);
        EventRole invitedSingleRole = eventRoleService.findByRole(EnumEventRole.INVITED);
        EventRole invitedGroupRole = eventRoleService.findByRole(EnumEventRole.GROUPINVITED);
        EventRole invitedSeriesGroupRole = eventRoleService.findByRole(EnumEventRole.GROUPSERIESINVITED);
        EventRole invitedSeriesRole = eventRoleService.findByRole(EnumEventRole.SERIESINVITED);
        EventRole invitedTutorRole = eventRoleService.findByRole(EnumEventRole.TUTORINVITED);
        EventRole invitedTutorSeriesRole = eventRoleService.findByRole(EnumEventRole.TUTORSERIESINVITED);

        try {
            if (userInEventWithRoleRepository.existsByUserAndEvent(user, event)) {
                UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(user, event);
                EventRole roleOfUser = userInEventWithRole.getEventRole();
                if (roleOfUser.equals(invitedSingleRole) || roleOfUser.equals(invitedGroupRole) || roleOfUser.equals(invitedSeriesRole) || roleOfUser.equals(invitedTutorRole) || roleOfUser.equals(invitedSeriesGroupRole) || roleOfUser.equals(invitedTutorSeriesRole)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks if user is already registered for event.
     * @param eventId ID of the event.
     * @param userMail Mail of the user.
     * @return True if attending, false if not
     */
    public Boolean isUserRegisteredToEvent(long eventId, String userMail) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserByMail(userMail);
        EventRole attendeeRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);
        EventRole groupAttendeeRole = eventRoleService.findByRole(EnumEventRole.GROUPATTENDEE);
        EventRole organizerRole = eventRoleService.findByRole(EnumEventRole.ORGANIZER);
        EventRole tutorRole = eventRoleService.findByRole(EnumEventRole.TUTOR);

        try {
            if (userInEventWithRoleRepository.existsByUserAndEvent(user, event)) {
                UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(user, event);
                EventRole roleOfUser = userInEventWithRole.getEventRole();
                if (roleOfUser.equals(attendeeRole) || roleOfUser.equals(groupAttendeeRole) || roleOfUser.equals(organizerRole) || roleOfUser.equals(tutorRole)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //endregion

    //region TutorMethods

    /**
     * Finds all attendees for an event and returns them in a list.
     * @param eventId ID of the event where the users attend.
     * @return List of users
     */
    public List<User> getAttendeesForEvent(long eventId) {
        try {
            Event event = eventService.getEventById(eventId);
            Set<UserInEventWithRole> userInEventWithRoleList = event.getEventUserRoles();
            EventRole attendeeRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);
            EventRole groupAttendeeRole = eventRoleService.findByRole(EnumEventRole.GROUPATTENDEE);
            EventRole tutorAttendeeRole = eventRoleService.findByRole(EnumEventRole.TUTOR);
            List<User> attendees = new ArrayList<>();
            for (UserInEventWithRole user : userInEventWithRoleList) {
                if (user.getEventRole().equals(attendeeRole) || user.getEventRole().equals(groupAttendeeRole) || user.getEventRole().equals(tutorAttendeeRole)) {
                    User attendee = user.getUser();
                    attendees.add(attendee);
                }
                //TODO: Gruppenhandhabung hinzufügen
            }
            return attendees;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Finds all connections between a given event and a list of users and returns their attending states.
     * @param eventId ID of the event where the users participate.
     * @param userIds List of IDs from users which participate at the event.
     * @return List of booleans in the order of the List of userIds.
     */
    public List<Boolean> getAttendingStatusForUsers(long eventId, List<Long> userIds) {
        List<Boolean> attendingStates = new ArrayList<>();
        try {
            for (long userId : userIds) {
                UserInEventWithRole uer = userInEventWithRoleRepository.findByUser_IdAndEvent_Id(userId, eventId);
                boolean attendingState = uer.isHasAttended();
                attendingStates.add(attendingState);
            }
            return attendingStates;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the attending status for given users at an event.
     * @param eventId ID of the event where the users attend.
     * @param userIds List of userIds from whom the attending status gets updated.
     * @param attending List of booleans which contains the new attending states.
     * @return String about success or failure.
     */
    public String updateAttendingStatusForUsers(long eventId, List<Long> userIds, List<Boolean> attending) {
        try {
            int i = 0;
            for (long userId : userIds) {
                boolean newIsAttending = attending.get(i);
                UserInEventWithRole userInEvent = userInEventWithRoleRepository.findByUser_IdAndEvent_Id(userId, eventId);
                userInEvent.setHasAttended(newIsAttending);
                i++;
                userInEventWithRoleRepository.save(userInEvent);
            }
            return LocalizedStringVariables.ATTENDINGSTATUSUPDATEDSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.ATTENDINGSTATUSUPDATEDFAILUREMESSAGE;
        }
    }

    /**
     * Adds a user to an event directly without an invitation.
     * @param eventId ID of the event.
     * @param userMail Mail of the user.
     * @return String about success or failure.
     */
    public String addUserToEvent(long eventId, String userMail) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserByMail(userMail);
        EventRole eventRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);
        try {
            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            userInEventWithRole.setEvent(event);
            userInEventWithRole.setUser(user);
            userInEventWithRole.setEventRole(eventRole);
            userInEventWithRole.setHasAttended(true);

            userInEventWithRoleRepository.save(userInEventWithRole);

            return LocalizedStringVariables.ADDEDUSERTOEVENTSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.ADDEDUSERTOEVENTFAILUREMESSAGE;
        }
    }

    //endregion

    //region OrganizerMethods

    /**
     * Gets all events where a specific user is organizer in a specific organisation.
     * @param userMail Mail of the organizer.
     * @param orgaId ID of the organisation.
     * @return List of the organizing events.
     */
    public List<Event> getManagingEvents(String userMail, long orgaId) {
        User organizer = userService.getUserByMail(userMail);
        EventRole organizerRole = eventRoleService.findByRole(EnumEventRole.ORGANIZER);

        List<Event> managingEvents = new ArrayList<>();

        try {
            List<UserInEventWithRole> userInEventWithRoleList = userInEventWithRoleRepository.findByUser_Id(organizer.getId());
            for (UserInEventWithRole userInEventWithRole : userInEventWithRoleList) {
                if (userInEventWithRole.getEventRole().equals(organizerRole)) {
                    Event managingEvent = userInEventWithRole.getEvent();
                    if (managingEvent.getOrganisation().getId() == orgaId) {
                        managingEvents.add(managingEvent);
                    }
                }
            }
            return managingEvents;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * An event is added to the database
     * @param event Event which is being added
     * @param userMail Mailaddress of the organizer.
     * @param orgaId ID of the organisation where the event belongs to.
     * @return String about success or failure
     */
    public MessageResponse createEventWithOrganizer(Event event, String userMail, long orgaId, MultipartFile image) {
        try {
            Organisation organisation = organisationService.getOrganisationById(orgaId);
            event.setStatus(EnumEventStatus.SCHEDULED);
            event.setOrganisation(organisation);
            eventService.saveEvent(event);
            if (image != null) {
                String imageUrl = documentService.saveEventImage(event.getId(), image);
                event.setImage(imageUrl);
            }
            setOrganizerOfEvent(userMail, event.getId());

            return  MessageResponse.builder()
                    .message(LocalizedStringVariables.EVENTCREATEDSUCCESSMESSAGE)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.EVENTCREATEDFAILUREMESSAGE)
                    .build();
        }
    }

    /**
     * A series of events is being created by creating multiple events with a specific time interval.
     * @param startEvent Event with the data for all other events and the start date of taking place.
     * @param eventSeries EventSeries with info about time interval between the events.
     * @param userMail Mail of the user who created the series and will be organizer.
     * @param orgaId ID of the organisation.
     * @return String about success or failure.
     */
    public String createEventSeriesWithOrganizer(Event startEvent, EventSeries eventSeries, String userMail, long orgaId, MultipartFile image) throws IOException {
        Organisation organisation = organisationService.getOrganisationById(orgaId);


        eventSeriesService.saveEventSeries(eventSeries);

        startEvent.setEventSeries(eventSeries);
        startEvent.setOrganisation(organisation);
        startEvent.setStatus(EnumEventStatus.SCHEDULED);
        eventService.saveEvent(startEvent);
        setOrganizerOfEvent(userMail, startEvent.getId());
        if (image != null) {
            String imageUrl = documentService.saveEventImage(startEvent.getId(), image);
            startEvent.setImage(imageUrl);
        }
        Set<Event> eventsOfSeries = new HashSet<>();
        eventsOfSeries.add(startEvent);

        Date startDate = startEvent.getStartDate();


        long diffInMillies = Math.abs(startEvent.getEndDate().getTime() - startEvent.getStartDate().getTime());
        long lengthOfEventInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);



        for (int i = 0; i < eventSeries.getAmount() - 1; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_MONTH, eventSeries.getDaysBetweenEvents());
            startDate = new java.sql.Date(calendar.getTimeInMillis());

            calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_MONTH, (int) lengthOfEventInDays);
            Date newEndDate = new java.sql.Date(calendar.getTimeInMillis());

            Event event = new Event();
            event.setName(startEvent.getName());
            event.setType(startEvent.getType());
            event.setStatus(startEvent.getStatus());
            event.setDescription(startEvent.getDescription());
            event.setImage(startEvent.getImage());
            event.setLocation(startEvent.getLocation());
            event.setStartDate(startDate);
            event.setStartTime(startEvent.getStartTime());
            event.setEndDate(newEndDate);
            event.setEndTime(startEvent.getEndTime());

            event.setOrganisation(organisation);
            event.setEventSeries(eventSeries);
            eventService.saveEvent(event);
            setOrganizerOfEvent(userMail, event.getId());
            eventsOfSeries.add(event);
        }
        eventSeries.setEvents(eventsOfSeries);


        try {
            //eventSeriesService.saveEventSeries(eventSeries);
            return LocalizedStringVariables.EVENTSERIESCREATEDSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.EVENTSERIESCREATEDFAILUREMESSAGE;
        }
    }



    /**
     * Sets a user as organizer for an event.
     * @param userMail ID of the user who will be organizer.
     * @param eventId Id of event where the user will be organizer.
     * @return String about success or failure.
     */
    public String setOrganizerOfEvent(String userMail, long eventId) {
        User user = userService.getUserByMail(userMail);
        Event event = eventService.getEventById(eventId);
        EventRole role = eventRoleService.findByRole(EnumEventRole.ORGANIZER);

        UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
        userInEventWithRole.setUser(user);
        userInEventWithRole.setEvent(event);
        userInEventWithRole.setEventRole(role);

        try {
            userInEventWithRoleRepository.save(userInEventWithRole);
            return LocalizedStringVariables.SETORGANIZEROFEVENTSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.SETORGANIZEROFEVENTFAILUREMESSAGE;
        }
    }

    /**
     * Changes the role of an already registered User to an event.
     * @param eventId ID of the event where the user attends.
     * @param userMail Mail address of the user whom role will be changed.
     * @param changeToAttendee Bool if he switches from attendee to tutor or from tutor to attendee.
     * @return String about success or failure.
     */
    public MessageResponse changeRoleOfPersonInEvent(long eventId, String userMail, boolean changeToAttendee) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserByMail(userMail);
        EventRole newRole;
        if (changeToAttendee)
        {
            newRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);
        } else {
            newRole = eventRoleService.findByRole(EnumEventRole.TUTOR);
        }

        if (userInEventWithRoleRepository.existsByUserAndEvent(user, event))
        {
            UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(user, event);
            userInEventWithRole.setEventRole(newRole);
            try{
                userInEventWithRoleRepository.save(userInEventWithRole);
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.CHANGEDROLEINEVENTSUCCESSMESSAGE)
                        .build();
            } catch (Exception e) {
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.CHANGEDROLEINEVENTFAILUREMESSAGE)
                        .build();
            }
        } else {
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.CHANGEDROLEINEVENTWITHOUTINVITATIONFAILUREMESSAGE)
                    .build();
        }
    }

    /**
     * Invites a user to an event.
     * @param eventId ID of the event.
     * @param userMail Mail of the user who will be invited.
     * @param single Boolean if invitation is just for him or because his group was invited.
     * @return String about success or failure.
     */
    public MessageResponse inviteUserToEvent(long eventId, String userMail, boolean single) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserByMail(userMail);

        EventRole inviteRole;
        if (single) {
            inviteRole = eventRoleService.findByRole(EnumEventRole.INVITED);
        } else {
            inviteRole = eventRoleService.findByRole(EnumEventRole.GROUPINVITED);
        }

        if (userInEventWithRoleRepository.existsByUserAndEvent(user, event)) {
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.USERALREADYPARTOFEVENTMESSAGE)
                    .build();
        } else {
            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            userInEventWithRole.setUser(user);
            userInEventWithRole.setEvent(event);
            userInEventWithRole.setEventRole(inviteRole);
            userInEventWithRole.setHasAttended(false);

            try {
                userInEventWithRoleRepository.save(userInEventWithRole);
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.INVITEUSERTOEVENTSUCCESSMESSAGE)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.INVITEUSERTOEVENTFAILUREMESSAGE)
                        .build();
            }
        }
    }

    /**
     * Invites a user extern to an event and sends an email
     * @param eventId Id of the corresponding event
     * @param userMail EMail of the corresponding user
     * @return success message
     */
    public MessageResponse inviteExternToEvent(long eventId, String userMail){
        Event event = eventService.getEventById(eventId);

        if(userService.getUserByMail(userMail) == null){
            try {

                mailMessage.setFrom("ftb-solutions@outlook.de");
                mailMessage.setTo(userMail);
                mailMessage.setSubject("Eventeinladung - "+event.getName());
                mailMessage.setText("Hallo,"
                        +"\nSie wurden zu dem Event "+ event.getName() +"eingeladen."
                        +"\nFalls Sie interesse haben, registrieren Sie sich unter:"
                        +"\n\t\t\tftb-eventmaster.de"
                        +"\nAnschließend können Sie sich zu unserer Organisation"
                        +"\n\t\t\t\t"+event.getOrganisation().getName()
                        +"\nregistrieren und sich für das Event anmelden."
                        +"\n"
                        +"\nWir freuen uns auf Ihre Teilnahme."
                        +"\nIhr "+event.getOrganisation().getName()+" - Team"
                        );
                emailService.sendEmail(mailMessage);
                //System.out.println(mailMessage.getText());


                return MessageResponse.builder()
                        .message(LocalizedStringVariables.INVITEUSERTOEVENTSUCCESSMESSAGE)
                        .build();

            } catch (Exception e) {
                e.printStackTrace();
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.INVITEUSERTOEVENTFAILUREMESSAGE)
                        .build();
            }
        }
        return MessageResponse.builder()
                .message(LocalizedStringVariables.INVITEUSERTOEVENTFAILUREMESSAGE)
                .build();
    }

    /**
     * Invites a tutor to an event.
     * @param eventId ID of the event.
     * @param userMail Mail of the user who will be invited as tutor.
     * @return String about success or failure.
     */
    public MessageResponse inviteTutorToEvent(long eventId, String userMail) {
        Event event = eventService.getEventById(eventId);
        User tutor = userService.getUserByMail(userMail);
        EventRole inviteRole = eventRoleService.findByRole(EnumEventRole.TUTORINVITED);

        if (userInEventWithRoleRepository.existsByUserAndEvent(tutor, event)) {
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.USERALREADYPARTOFEVENTMESSAGE)
                    .build();
        } else {
            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            userInEventWithRole.setUser(tutor);
            userInEventWithRole.setEvent(event);
            userInEventWithRole.setEventRole(inviteRole);
            userInEventWithRole.setHasAttended(false);
            try {
                userInEventWithRoleRepository.save(userInEventWithRole);
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.INVITETUTORTOEVENTSUCCESSMESSAGE)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.INVITETUTORTOEVENTFAILUREMESSAGE)
                        .build();
            }
        }
    }

    /**
     * Invites a user to the first event of a series.
     * @param eventSeries Series of events from which the event is the first one.
     * @param eventId ID of the event which is the first of the series.
     * @param userMail Mail of the user who will be invited.
     * @return String about success or failure.
     */
    public String inviteUserToFirstEventFromSeries(EventSeries eventSeries, long eventId, String userMail, boolean byGroup) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserByMail(userMail);

        EventRole eventRole;
        if (byGroup) {
            eventRole = eventRoleService.findByRole(EnumEventRole.GROUPSERIESINVITED);
        } else  {
            eventRole = eventRoleService.findByRole(EnumEventRole.SERIESINVITED);
        }


        if (userInEventWithRoleRepository.existsByUserAndEvent(user, event)) {
            return LocalizedStringVariables.USERALREADYPARTOFEVENTMESSAGE;
        } else {
            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            userInEventWithRole.setUser(user);
            userInEventWithRole.setEvent(event);
            userInEventWithRole.setEventRole(eventRole);
            userInEventWithRole.setHasAttended(false);

            try {
                userInEventWithRoleRepository.save(userInEventWithRole);
                //TODO Einladungsmail senden mit Anmerkung zu Series
                return LocalizedStringVariables.INVITEUSERTOFIRSTEVENTOFSERIESSUCCESSMESSAGE;
            } catch (Exception e) {
                e.printStackTrace();
                return LocalizedStringVariables.INVITEUSERTOFIRSTEVENTOFSERIESFAILUREMESSAGE;
            }
        }
    }

    /**
     * Removes a user from an event and sends the user an email why.
     * @param eventId ID of the event.
     * @param userMail Mail of the user who will be removed.
     * @return String about success or failure.
     */
    public MessageResponse removeUserFromEvent(long eventId, String userMail) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserByMail(userMail);

        if (userInEventWithRoleRepository.existsByUserAndEvent(user, event)) {
            UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(user, event);
            EventRole organizerRole = eventRoleService.findByRole(EnumEventRole.ORGANIZER);
            if (userInEventWithRole.getEventRole().equals(organizerRole))
            {
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.REMOVEORGANIZERFROMEVENTNOTPOSSIBLE)
                        .build();
            } else {
                try {
                    userInEventWithRoleRepository.delete(userInEventWithRole);
                    return MessageResponse.builder()
                            .message(LocalizedStringVariables.REMOVEUSERFROMEVENTSUCCESSMESSAGE)
                            .build();
                } catch (Exception e) {
                    e.printStackTrace();
                    return MessageResponse.builder()
                            .message(LocalizedStringVariables.REMOVEUSERFROMEVENTFAILUREMESSAGE)
                            .build();
                }
            }
        } else {
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.USERNOTATTENDINGATEVENTMESSAGE)
                    .build();
        }
    }

    /**
     * Removes the users from an event if they were invited or attending by a group.
     * @param eventId ID of the event.
     * @param users List of the users who are part of the group.
     * @param reason Reason why they will be removed.
     * @return String about success or failure.
     */
    public String removeUsersOfGroupFromEvent(long eventId, List<User> users, String reason) {
        Event event = eventService.getEventById(eventId);
        EventRole groupAttending = eventRoleService.findByRole(EnumEventRole.GROUPATTENDEE);
        EventRole groupInvited = eventRoleService.findByRole(EnumEventRole.GROUPINVITED);
        EventRole groupSeriesInvited = eventRoleService.findByRole(EnumEventRole.GROUPSERIESINVITED);


        try {
            for (User user : users) {
                if (userInEventWithRoleRepository.existsByUserAndEvent(user, event)) {
                    UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(user, event);
                    if (userInEventWithRole.getEventRole().equals(groupAttending)) {
                        //TODO reason senden weil als gruppenmitglied teilnehmer
                        userInEventWithRoleRepository.delete(userInEventWithRole);
                    } else if (userInEventWithRole.getEventRole().equals(groupInvited)) {
                        //TODO reason senden weil als gruppenmitglied eingeladen worden
                    } else if (userInEventWithRole.getEventRole().equals(groupSeriesInvited)) {
                        //TODO reason senden weil als gruppenmitgied zu serie eingeladen worden
                    }
                    userInEventWithRoleRepository.delete(userInEventWithRole);
                }
            }
            return LocalizedStringVariables.REMOVEDUSERSOFGROUPFROMEVENTSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.REMOVEDUSERSOFGROUPFROMEVENTFAILUREMESSAGE;
        }
    }
    /** Method to get all affiliated users for the event
     * @param eventId EventId to be checked.
     * @return List of UserInEventWithRole that are affiliated with the event.
     */
    public List<UserInEventWithRole> findByEventId(long eventId){
        return userInEventWithRoleRepository.findByEvent_Id(eventId);
    }
    //endregion

    //region AdminMethods

    /**
     * Change the organizer of the event by removing the old organizer of the event and setting the new user as organizer of the event.
     * @param eventId ID of the event.
     * @param userMail Mail address of the new organizer.
     * @return String about success or failure.
     */
    public MessageResponse changeOrganizerOfEvent(long eventId, String userMail) {
        User newOrganizer = userService.getUserByMail(userMail);
        Event event = eventService.getEventById(eventId);
        EventRole eventRole = eventRoleService.findByRole(EnumEventRole.ORGANIZER);

        try {
            UserInEventWithRole oldUserInEventWithRole = userInEventWithRoleRepository.findByEventAndEventRole(event, eventRole);
            userInEventWithRoleRepository.delete(oldUserInEventWithRole);
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.USERINEVENTWITHROLEDELETIONFAILUREMESSAGE)
                    .build();
        }

        if (!userInEventWithRoleRepository.existsByUser_IdAndEvent_Id(newOrganizer.getId(), eventId)) {
            UserInEventWithRole newUserInEventWithRole = new UserInEventWithRole();
            newUserInEventWithRole.setUser(newOrganizer);
            newUserInEventWithRole.setEvent(event);
            newUserInEventWithRole.setEventRole(eventRole);
            try {
                userInEventWithRoleRepository.save(newUserInEventWithRole);
            } catch (Exception e) {
                e.printStackTrace();
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.NEWUSERASORGANIZERINEVENTFAILUREMESSAGE)
                        .build();
            }
        } else {
            UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(newOrganizer, event);
            userInEventWithRole.setEventRole(eventRole);
            try {
                userInEventWithRoleRepository.save(userInEventWithRole);
            } catch (Exception e) {
                e.printStackTrace();
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.NEWUSERASORGANIZERINEVENTFAILUREMESSAGE)
                        .build();
            }
        }

        return MessageResponse.builder()
                .message(LocalizedStringVariables.CHANGEDORGANIZEROFEVENTSUCCESSMESSAGE)
                .build();
    }

    public List<Event> getEventInvitationsForUserInOrga(long orgaId, String emailAdress) {
        try {
            User user = userService.getUserByMail(emailAdress);
            List<UserInEventWithRole> userInEventWithRoleList = userInEventWithRoleRepository.findByUser_Id(user.getId());

            List<Event> eventsOfOrganisation = eventService.getEventsOfOrganisation(orgaId);

            List<Event> eventInvites = new ArrayList<>();

            for(Event event: eventsOfOrganisation){
                for(UserInEventWithRole userInEventWithRole: userInEventWithRoleList){
                    if(userInEventWithRole.getEvent().getId() == event.getId()){
                        if (isUserInvitedToEvent(event.getId(), emailAdress)) {
                            eventInvites.add(event);
                        }
                    }
                }
            }
            return eventInvites;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //endregion
}
