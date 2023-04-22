package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.UserInEventWithRoleRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private final EventRoleService eventRoleService;


    public UserInEventWithRoleService(
            UserInEventWithRoleRepository userInEventWithRoleRepository,
            UserService userService,
            EventService eventService,
            EventRoleService eventRoleService) {
        this.userInEventWithRoleRepository = userInEventWithRoleRepository;
        this.eventService = eventService;
        this.userService = userService;
        this.eventRoleService = eventRoleService;
    }


    //region UserMethods

    /**
     * A user is being registered for an event
     * @param eventId Id of the corresponding event
     * @param userId Id of the corresponding user
     * @return success message
     */
    public String registerForEvent(long eventId, long userId){
        try {

            EventRole eventRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);

            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            userInEventWithRole.setEvent(eventService.getEventById(eventId));
            userInEventWithRole.setUser(userService.getUserById(userId));
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
     * @param userId Id of the user who is being invited
     * @return success message
     */
    public String acceptEventInvitation(long eventId, long userId){
        try {
            EventRole eventRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);

            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            //Todo Abfragen von User oder Event in if-Abfragen "abfangen" im Fehlerfall
            userInEventWithRole.setEvent(eventService.getEventById(eventId));
            userInEventWithRole.setUser(userService.getUserById(userId));
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
     * @param userId Id of the user who is being invited
     * @return success message
     */
    public String declineEventInvitation(long eventId, long userId){
        try {

            //TODO hier einfach das userInEventWithRole Objekt löschen


            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            userInEventWithRole.setEvent(eventService.getEventById(eventId));
            userInEventWithRole.setUser(userService.getUserById(userId));
            // userInEventWithRole.setEventRole(eventRole);
            userInEventWithRoleRepository.save(userInEventWithRole);

            return LocalizedStringVariables.EVENTINVITATIONDECLINEDSUCCESSMESSAGE;
        }catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.EVENTINVITATIONDECLINEDFAILUREMESSAGE;
        }
    }

    /**
     * For authorization purposes u can get your role corresponding to an event
     * @param eventId Id of the event the user is requesting his role for
     * @param userId Id of the user who is requesting the role
     * @return Role of the user
     */
    public EventRole getRoleForEvent(long eventId, long userId){
        try {

            User user = userService.getUserById(userId);
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
     * A user can get all events he is registered for
     * @param userId Id of the user to get registered events for
     * @return List of events the user is registered for
     */
    public List<Event> getRegisteredEventsForUser(long userId){
        try {
            EventRole eventRole = eventRoleService.findByRole(EnumEventRole.ATTENDEE);
            User user = userService.getUserById(userId);

            List<UserInEventWithRole> eventsForUser =  userInEventWithRoleRepository.findByUser(user);
            List<Event> registeredEventsForUser = null;
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
     * @param event Corresponding event
     * @param userId Id of the user who is about to unregister
     * @param reason Reason for unregistering as a feedback for the organizer
     * @return success message
     */
    public String unregisterFromEvent(Event event,long userId, String reason){
        //Todo reason speichern
        try {

            User user = userService.getUserById(userId);
            UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(user, event);
            userInEventWithRoleRepository.delete(userInEventWithRole);

            return LocalizedStringVariables.USERUNREGISTERFROMEVENTSUCCESSMESSAGE;
        }catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.USERUNREGISTERFROMEVENTFAILUREMESSAGE;
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
     * Sets a user as organizer for an event.
     * @param userId ID of the user who will be organizer.
     * @param eventId ID of the event.
     * @return String about success or failure.
     */
    public String setOrganizerOfEvent(long userId, long eventId) {
        User user = userService.getUserById(userId);
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
            userInEventWithRole.setHasAttended(true);

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


        try {
            for (User user : users) {
                if (userInEventWithRoleRepository.existsByUserAndEvent(user, event)) {
                    UserInEventWithRole userInEventWithRole = userInEventWithRoleRepository.findByUserAndEvent(user, event);
                    if (userInEventWithRole.getEventRole().equals(groupAttending)) {
                        //TODO reason senden weil als gruppenmitglied teilnehmer
                        userInEventWithRoleRepository.delete(userInEventWithRole);
                    } else if (userInEventWithRole.getEventRole().equals(groupInvited)) {
                        //TODO reason senden weil als gruppenmitglied eingeladen worden
                        userInEventWithRoleRepository.delete(userInEventWithRole);
                    }
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

        if (userInEventWithRoleRepository.findByUserAndEvent(newOrganizer, event) == null) {
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
