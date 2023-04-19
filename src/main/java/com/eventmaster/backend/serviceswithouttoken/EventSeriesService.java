package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.EventSeries;
import com.eventmaster.backend.repositories.EventSeriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of EventSerieses
 *
 * @author Fabian Unger
 */
@Service
public class EventSeriesService {
    private final EventSeriesRepository eventSeriesRepository;
    private final EventService eventService;

    public EventSeriesService(EventSeriesRepository eventSeriesRepository, EventService eventService) {
        this.eventSeriesRepository = eventSeriesRepository;
        this.eventService = eventService;
    }

    public EventSeries getEventSeriesById(long eventSeriesId) {
        try {
            return eventSeriesRepository.findById(eventSeriesId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A series of events is being created by creating multiple events with a specific time interval.
     * @param lastEvent Event with the data for all other events and the last date of taking place.
     * @param eventSeries EventSeries with info about time interval between the events.
     * @return String about success or failure.
     */
    public String createEventSeries(Event lastEvent, EventSeries eventSeries) {
        try {
            //TODO: Von heute an bis zum lastEvent in dem Abstand aus eventSeries die Events erstellen und zur Liste in eventSeries hinzufügen
            return "EventSeries created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Creation failed";
        }
    }

    /**
     * A series of events is being changed.
     * @param lastEvent New event with the data for all other events and the last date of taking place.
     * @param eventSeries New eventSeries with info about time interval between the events.
     * @return String about success or failure.
     */
    public String changeEventSeries(Event lastEvent, EventSeries eventSeries) {
        try {
            long eventSeriesId = eventSeries.getId();

            EventSeries oldEventSeries = getEventSeriesById(eventSeriesId);
            Set<Event> EventSeries = oldEventSeries.getEvents();

            //TODO: Alle Events durchgehen und Daten abändern.

            return "EventSeries changed successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Change failed";
        }
    }

    /**
     * Changes the status of the events which belong to a series of events.
     * @param eventSeriesId ID of the EventSeries
     * @param newStatus New status for the events.
     * @return String about success or failure.
     */
    public String changeStatusOfEventSeries(long eventSeriesId, String newStatus) {
        try {
            EventSeries eventSeries = getEventSeriesById(eventSeriesId);

            Set<Event> events = eventSeries.getEvents();

            for (Event event : events) {
                if (!event.getStatus().equals("abgeschlossen")) {
                    event.setStatus(newStatus);
                    eventService.changeEvent(event);
                }
            }

            eventSeriesRepository.save(eventSeries);
            return "Status changed successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Change of status failed";
        }
    }

    /**
     * A series of events is being deleted from the database.
     * @param eventSeriesId ID of the EventSeries which contains the series of events.
     * @return String about success or failure.
     */
    public String deleteEventSeries(long eventSeriesId) {
        try {
            EventSeries eventSeries = getEventSeriesById(eventSeriesId);
            Set<Event> events = eventSeries.getEvents();

            for (Event event : events) {
                long eventId = event.getId();
                this.eventService.deleteEvent(eventId);
            }

            this.eventSeriesRepository.deleteById(eventSeriesId);

            return "Eventseries deleted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Deletion failed";
        }
    }
}
