package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.User;
import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A class which receives and processes the requests of the controller
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
            System.out.println("hello World");
            eventRepository.save(event);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * An event corresponding to the eventId is being deleted
     * @param eventId ID of the event which will be deleted.
     * @return Boolen as status for succes
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
