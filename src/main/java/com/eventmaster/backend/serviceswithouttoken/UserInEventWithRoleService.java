package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.EventRole;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.entities.UserInEventWithRole;
import com.eventmaster.backend.repositories.UserInEventWithRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of events
 *
 * @author Fabian Eilber
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


    //UserMethods

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

            EventRole eventRole = eventRoleService.findByRole("ACCEPTED");


            UserInEventWithRole userInEventWithRole = new UserInEventWithRole();
            userInEventWithRole.setEvent(eventService.getEventById(eventId));
            userInEventWithRole.setUser(userService.getUserById(userId));
            userInEventWithRole.setEventRole(eventRole);
            userInEventWithRoleRepository.save(userInEventWithRole);

            return "Succesfully accepted the invitation";
        }catch (Exception e) {
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

            User user = userService.getUserById(userId);
            UserInEventWithRole unregisterEvent = userInEventWithRoleRepository.findByUserAndEvent(user, event);
            userInEventWithRoleRepository.delete(unregisterEvent);

            return "Unregister from event" + event.getName() + "was successfully" ;
        }catch (Exception e) {
            e.printStackTrace();
            return "Unregister from event" + event.getName() +"was not successfully";
        }

    }
/*
    public List<Event> getAllEventsForUser(long userId){
       //Todo implementieren der inneren Login
    }

 */
}
