package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.UserInEventWithRoleRepository;
import com.eventmaster.backend.security.authentification.VerificationResponse;
import local.variables.LocalizedStringVariables;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
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
    private final EventSeriesService eventSeriesService;
    private final EventRoleService eventRoleService;


    public UserInEventWithRoleService(
            UserInEventWithRoleRepository userInEventWithRoleRepository,
            UserService userService,
            EventService eventService,
            EventSeriesService eventSeriesService, EventRoleService eventRoleService) {
        this.userInEventWithRoleRepository = userInEventWithRoleRepository;
        this.eventService = eventService;
        this.userService = userService;
        this.eventSeriesService = eventSeriesService;
        this.eventRoleService = eventRoleService;
    }


    //region UserMethods

    /**
     * A user is being registered for an event
     * @param eventId Id of the corresponding event
     * @param emailAdress Email of the corresponding user
     * @return success message
     */
    public String registerForEvent(long eventId, String emailAdress){
        try {

            EventRole eventRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);

            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            userInEventWithRole.setEvent(eventService.getEventById(eventId));
            userInEventWithRole.setUser(userService.getUserByMail(emailAdress));
            userInEventWithRole.setEventRole(eventRole);
            userInEventWithRoleRepository.save(userInEventWithRole);

            return LocalizedStringVariables.USERREGISTERESFOREVENTSUCCESSMESSAGE;
        }catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.USERREGISTERESFOREVENTFAILUREMESSAGE;
        }
    }

    /**
     * A user can accept an invitation to an event
     * @param eventId Id of the event the user is invited to
     * @param emailAdress Email of the user who is being invited
     * @return success message
     */
    public String acceptEventInvitation(long eventId, String emailAdress){
        try {
            EventRole eventRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);

            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            //Todo Abfragen von User oder Event in if-Abfragen "abfangen" im Fehlerfall
            userInEventWithRole.setEvent(eventService.getEventById(eventId));
            userInEventWithRole.setUser(userService.getUserByMail(emailAdress));
            userInEventWithRole.setEventRole(eventRole);
            userInEventWithRoleRepository.save(userInEventWithRole);

            return LocalizedStringVariables.EVENTINVITATIONACCEPTEDSUCCESSMESSAGE;
        }catch (Exception e) {
            //Todo Exceptionhandling weiter ausbauen, sodass Nutzer weiß was genau passiert ist
            e.printStackTrace();
            return LocalizedStringVariables.EVENTINVITATIONACCEPTEDFAILUREMESSAGE;
        }
    }

    /**
     * A user can decline an invitation to an event
     * @param eventId Id of the event the user is invited to
     * @param emailAdress Email of the user who is being invited
     * @return success message
     */
    public String declineEventInvitation(long eventId, String emailAdress){
        try {
            User user = userService.getUserByMail(emailAdress);
            Event event = eventService.getEventById(eventId);

            UserInEventWithRole userInEventWithRole= userInEventWithRoleRepository.findByUserAndEvent(user, event);

            if(userInEventWithRole!=null) {
                userInEventWithRoleRepository.delete(userInEventWithRole);
            }
            return LocalizedStringVariables.EVENTINVITATIONDECLINEDSUCCESSMESSAGE;
        }catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.EVENTINVITATIONDECLINEDFAILUREMESSAGE;
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

            EventRole eventRole = userInEvent.getEventRole();

            return eventRole;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Retrieving all events for a user
     * @param emailAdress Email of the corresponding user
     * @return List of Events
     */
    public List<Event> getAllEventsForUser(String emailAdress){
        try {
            User user = userService.findByEmailAdress(emailAdress);

            List<UserInEventWithRole> userInEvents = userInEventWithRoleRepository.findByUser_Id(user.getId());

            List<Event> userEvents = new ArrayList<>();

            for (UserInEventWithRole event: userInEvents) {
                if(event != null){
                    userEvents.add(event.getEvent());
                }
            }


            return userEvents;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * A user can get all events he is registered for
     * @param emailAdress Email of the user to get registered events for
     * @return List of events the user is registered for
     */
    public List<Event> getRegisteredEventsForUser(String emailAdress){
        try {
            EventRole eventRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);
            User user = userService.findByEmailAdress(emailAdress);

            List<UserInEventWithRole> eventsForUser =  userInEventWithRoleRepository.findByUser_Id(user.getId());

            List<Event> registeredEventsForUser = new ArrayList<>();
            for(UserInEventWithRole event: eventsForUser){
                if(event.getEventRole().equals(eventRole)) {
                    registeredEventsForUser.add(event.getEvent());
                }
            }
            return registeredEventsForUser;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A user can unregister from an event
     * @param eventId Corresponding event
     * @param emailAdress Id of the user who is about to unregister
     * @param reason Reason for unregistering as a feedback for the organizer
     * @return success message
     */
    public String unregisterFromEvent(long eventId,String emailAdress, String reason){
        //Todo reason speichern
        try {

            User user = userService.getUserByMail(emailAdress);
            Event event = eventService.getEventById(eventId);
            UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(user, event);
            userInEventWithRoleRepository.delete(userInEventWithRole);

            return LocalizedStringVariables.USERUNREGISTERFROMEVENTSUCCESSMESSAGE;
        }catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.USERUNREGISTERFROMEVENTFAILUREMESSAGE;
        }

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
        EventRole invitedSeriesRole = eventRoleService.findByRole(EnumEventRole.SERIESINVITED);

        try {
            if (userInEventWithRoleRepository.existsByUserAndEvent(user, event)) {
                UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(user, event);
                EventRole roleOfUser = userInEventWithRole.getEventRole();
                if (roleOfUser.equals(invitedSingleRole) || roleOfUser.equals(invitedGroupRole) || roleOfUser.equals(invitedSeriesRole)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

/*
    public List<Event> getAllEventsForUser(long userId){
       //Todo implementieren der inneren Login
    }

 */



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
            List<User> attendees = new ArrayList<>();
            for (UserInEventWithRole uer : userInEventWithRoleList) {
                if (uer.getEventRole().equals(attendeeRole)) {
                    User attendee = uer.getUser();
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
     * An event is added to the database
     * @param event Event which is being added
     * @return String about success or failure
     */
    public MessageResponse createEventWithOrganizer(Event event, String userMail) {
        try {
            eventService.saveEvent(event);
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
     * @return String about success or failure.
     */
    public String createEventSeriesWithOrganizer(Event startEvent, EventSeries eventSeries, String userMail) {
        int intervalInMilliseconds = eventSeries.getDaysBetweenEvents() * 86400000;

        Set<Event> eventsOfSeries = new HashSet<>();
        eventsOfSeries.add(startEvent);

        Date startDate = startEvent.getStartDate();


        long diffInMillies = Math.abs(startEvent.getEndDate().getTime() - startEvent.getStartDate().getTime());
        long lengthOfEventInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);


        for (int i = 0; i < eventSeries.getAmount(); i++) {
            //Hier StartDate berechnen
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

            event.setOrganisation(startEvent.getOrganisation());
            event.setEventSeries(eventSeries);
            setOrganizerOfEvent(userMail, event.getId());
            eventService.saveEvent(event);
            eventsOfSeries.add(event);
        }
        eventSeries.setEvents(eventsOfSeries);


        try {
            eventSeriesService.saveEventSeries(eventSeries);
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
    public String changeRoleOfPersonInEvent(long eventId, String userMail, boolean changeToAttendee) {
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
                return LocalizedStringVariables.CHANGEDROLEINEVENTSUCCESSMESSAGE;
            } catch (Exception e) {
                return LocalizedStringVariables.CHANGEDROLEINEVENTFAILUREMESSAGE;
            }
        } else {
            return LocalizedStringVariables.CHANGEDROLEINEVENTWITHOUTINVITATIONFAILUREMESSAGE;
        }
    }

    /**
     * Invites a user to an event and sends an email.
     * @param eventId ID of the event.
     * @param userMail Mail of the user who will be invited.
     * @param single Boolean if invitation is just for him or because his group was invited.
     * @return String about success or failure.
     */
    public String inviteUserToEvent(long eventId, String userMail, boolean single) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserByMail(userMail);

        EventRole inviteRole;
        if (single) {
            inviteRole = eventRoleService.findByRole(EnumEventRole.INVITED);
        } else {
            inviteRole = eventRoleService.findByRole(EnumEventRole.GROUPINVITED);
        }

        if (userInEventWithRoleRepository.existsByUserAndEvent(user, event)) {
            return LocalizedStringVariables.USERALREADYPARTOFEVENTMESSAGE;
        } else {
            //TODO Einladungsmail senden

            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            userInEventWithRole.setUser(user);
            userInEventWithRole.setEvent(event);
            userInEventWithRole.setEventRole(inviteRole);
            userInEventWithRole.setHasAttended(false);

            try {
                userInEventWithRoleRepository.save(userInEventWithRole);
                return LocalizedStringVariables.INVITEUSERTOEVENTSUCCESSMESSAGE;
            } catch (Exception e) {
                e.printStackTrace();
                return LocalizedStringVariables.INVITEUSERTOEVENTFAILUREMESSAGE;
            }
        }
    }

    /**
     * Invites a tutor to an event.
     * @param eventId ID of the event.
     * @param userMail Mail of the user who will be invited as tutor.
     * @return String about success or failure.
     */
    public String inviteTutorToEvent(long eventId, String userMail) {
        Event event = eventService.getEventById(eventId);
        User tutor = userService.getUserByMail(userMail);
        EventRole inviteRole = eventRoleService.findByRole(EnumEventRole.TUTORINVITED);

        if (userInEventWithRoleRepository.existsByUserAndEvent(tutor, event)) {
            return LocalizedStringVariables.USERALREADYPARTOFEVENTMESSAGE;
        } else {
            //TODO Einladungsmail senden
            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            userInEventWithRole.setUser(tutor);
            userInEventWithRole.setEvent(event);
            userInEventWithRole.setEventRole(inviteRole);
            userInEventWithRole.setHasAttended(false);
            try {
                userInEventWithRoleRepository.save(userInEventWithRole);
                return LocalizedStringVariables.INVITETUTORTOEVENTSUCCESSMESSAGE;
            } catch (Exception e) {
                e.printStackTrace();
                return LocalizedStringVariables.INVITETUTORTOEVENTFAILUREMESSAGE;
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
     * @param reason Reason why he will be removed.
     * @return String about success or failure.
     */
    public String removeUserFromEvent(long eventId, String userMail, String reason) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserByMail(userMail);

        if (userInEventWithRoleRepository.existsByUserAndEvent(user, event)) {
            UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(user, event);
            try {
                userInEventWithRoleRepository.delete(userInEventWithRole);
                //TODO Mail mit reason senden
                return LocalizedStringVariables.REMOVEUSERFROMEVENTSUCCESSMESSAGE;
            } catch (Exception e) {
                e.printStackTrace();
                return LocalizedStringVariables.REMOVEUSERFROMEVENTFAILUREMESSAGE;
            }
        } else {
            return LocalizedStringVariables.USERNOTATTENDINGATEVENTMESSAGE;
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

    //endregion

    //region AdminMethods

    /**
     * Change the organizer of the event by removing the old organizer of the event and setting the new user as organizer of the event.
     * @param eventId ID of the event.
     * @param userMail Mail address of the new organizer.
     * @return String about success or failure.
     */
    public String changeOrganizerOfEvent(long eventId, String userMail) {
        User newOrganizer = userService.getUserByMail(userMail);
        Event event = eventService.getEventById(eventId);
        EventRole eventRole = eventRoleService.findByRole(EnumEventRole.ORGANIZER);

        try {
            UserInEventWithRole oldUserInEventWithRole = userInEventWithRoleRepository.findByEventAndEventRole(event, eventRole);
            userInEventWithRoleRepository.delete(oldUserInEventWithRole);
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.USERINEVENTWITHROLEDELETIONFAILUREMESSAGE;
        }

        if (!userInEventWithRoleRepository.existsByUserAndEvent(newOrganizer, event)) {
            UserInEventWithRole newUserInEventWithRole = new UserInEventWithRole();
            newUserInEventWithRole.setUser(newOrganizer);
            newUserInEventWithRole.setEvent(event);
            newUserInEventWithRole.setEventRole(eventRole);
            try {
                userInEventWithRoleRepository.save(newUserInEventWithRole);
            } catch (Exception e) {
                e.printStackTrace();
                return LocalizedStringVariables.NEWUSERASORGANIZERINEVENTFAILUREMESSAGE;
            }
        } else {
            UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(newOrganizer, event);
            userInEventWithRole.setEventRole(eventRole);
            try {
                userInEventWithRoleRepository.save(userInEventWithRole);
            } catch (Exception e) {
                e.printStackTrace();
                return LocalizedStringVariables.EXISTINGUSERASORGANIZERINEVENTSUCCESSMESSAGE;
            }
        }

        return LocalizedStringVariables.CHANGEDORGANIZEROFEVENTSUCCESSMESSAGE;
    }

    //endregion
}
