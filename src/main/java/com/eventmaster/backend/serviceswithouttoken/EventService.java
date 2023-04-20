package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A class which receives and processes the requests of the controller
 *
 * @author Fabian Eilber
 * @author Fabian Unger
 */

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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
     * An event is added to the database
     * @param event Event which is being added
     * @return String about success or failure
     */
    public String createEvent(Event event) {
        try {
            eventRepository.save(event);
            return "Event created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Event creation failed";
        }
    }

    /**
     * An existing event in the database is being changed.
     * @param event New event with the new data.
     * @return String about success or failure.
     */
    public String changeEvent(Event event) {
        try {
            eventRepository.save(event);
            return "Event changed successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Change failed";
        }
    }

    /**
     * Changing the status of an event.
     * @param eventId ID of the event from which the status will be changed.
     * @param newStatus New status of the event.
     * @return String about success or failure.
     */
    public String changeStatusOfEvent(long eventId, String newStatus) {
        try {
            Event event = getEventById(eventId);
            event.setStatus(newStatus);

            this.eventRepository.save(event);

            return "Status changed successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Change of status failed";
        }
    }

    /**
     * An event corresponding to the eventId is being deleted
     * @param eventId ID of the event which will be deleted.
     * @return Boolen as status for success
     */
    public String deleteEvent(Long eventId){
        try {
            eventRepository.deleteById(eventId);
            return "Event deleted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Deletion failed";
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

}
