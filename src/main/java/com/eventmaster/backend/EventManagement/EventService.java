package com.eventmaster.backend.EventManagement;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * A class which receive and process the requests of the controller
 *
 * @author Fabian Eilber
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
     * The database is searched for events with the corresponding ID
     * @param eventId ID of the event which will be searched.
     * @return Event Object
     */
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    /**
     * An event is added to the databse
     * @param event Event which is being added
     * @return Boolen as status for succes
     */
    public boolean createEvent(Event event) {
        try {
            eventRepository.save(event);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * An event corresponding to teh eventId is being deleted
     * @param eventId ID of the event which will be deleted.
     */
    public boolean deleteEvent(Long eventId){
        try {
            eventRepository.deleteById(eventId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
