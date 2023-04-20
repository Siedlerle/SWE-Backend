package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.EventRole;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.entities.UserInEventWithRole;
import com.eventmaster.backend.repositories.UserInEventWithRoleRepository;
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

            EventRole eventRole = eventRoleService.findByRole("REGISTERED");

            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            userInEventWithRole.setEvent(eventService.getEventById(eventId));
            userInEventWithRole.setUser(userService.getUserById(userId));
            userInEventWithRole.setEventRole(eventRole);
            userInEventWithRoleRepository.save(userInEventWithRole);

            return "Succesfully registered for Event";
        }catch (Exception e) {
            e.printStackTrace();
            return "Error during registration";
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

            //Todo Enum für roleName nutzen
            EventRole eventRole = eventRoleService.findByRole("ACCEPTED");

            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            //Todo Abfragen von User oder Event in if-Abfragen "abfangen" im Fehlerfall
            userInEventWithRole.setEvent(eventService.getEventById(eventId));
            userInEventWithRole.setUser(userService.getUserById(userId));
            userInEventWithRole.setEventRole(eventRole);
            userInEventWithRoleRepository.save(userInEventWithRole);

            //Todo return Strings nicht wie hier festlegen. Strings auslagern als resource
            return "Succesfully accepted the invitation";
        }catch (Exception e) {
            //Todo Exceptionhandling weiter ausbauen, sodass Nutzer weiß was genau passiert ist
            e.printStackTrace();
            return "Error during registration";
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

            EventRole eventRole = eventRoleService.findByRole("DECLINED");


            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            userInEventWithRole.setEvent(eventService.getEventById(eventId));
            userInEventWithRole.setUser(userService.getUserById(userId));
            userInEventWithRole.setEventRole(eventRole);
            userInEventWithRoleRepository.save(userInEventWithRole);

            return "Succesfully declined the invitation";
        }catch (Exception e) {
            e.printStackTrace();
            return "Error during registration";
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

            User user = userService.getUserById(userId);

            List<UserInEventWithRole> eventsForUser =  userInEventWithRoleRepository.findByUser(user);
            List<Event> registeredEventsForUser = null;
            for(UserInEventWithRole event: eventsForUser){
                if(event.getEventRole().equals("REGISTERED")){
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

            //Todo Variablenbennenung anpassen
            User user = userService.getUserById(userId);
            UserInEventWithRole unregisterEvent = userInEventWithRoleRepository.findByUserAndEvent(user, event);
            userInEventWithRoleRepository.delete(unregisterEvent);

            return "Unregister from event " + event.getName() + " was successfully" ;
        }catch (Exception e) {
            e.printStackTrace();
            return "Unregister from event " + event.getName() +" was not successfully";
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
            List<User> attendees = new ArrayList<>();
            for (UserInEventWithRole uer : userInEventWithRoleList) {
                //TODO: Nur wenn Rolle Teilnehmer ist zur Liste hinzufügen.
                User attendee = uer.getUser();
                attendees.add(attendee);
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
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
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
        try {
            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            userInEventWithRole.setEvent(event);
            userInEventWithRole.setUser(user);
            //TODO: Rolle Teilnehmer festlegen beim Hinzufügen eines Teilnehmers durch den Tutor.
            // userInEventWithRole.setEventRole();
            userInEventWithRole.setHasAttended(true);

            userInEventWithRoleRepository.save(userInEventWithRole);

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
    }

    //endregion


}
