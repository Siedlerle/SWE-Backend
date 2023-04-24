package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.EnumEventStatus;
import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.EventSeries;
import com.eventmaster.backend.repositories.EventSeriesRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final UserInEventWithRoleService userInEventWithRoleService;

    public EventSeriesService(EventSeriesRepository eventSeriesRepository, EventService eventService, UserInEventWithRoleService userInEventWithRoleService) {
        this.eventSeriesRepository = eventSeriesRepository;
        this.eventService = eventService;
        this.userInEventWithRoleService = userInEventWithRoleService;
    }

    public EventSeries getEventSeriesById(long eventSeriesId) {
        try {
            return eventSeriesRepository.findById(eventSeriesId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void saveEventSeries(EventSeries eventSeries) {
        eventSeriesRepository.save(eventSeries);
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

            return LocalizedStringVariables.EVENTSERIESCHANGEDSUCCESSMESAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.EVENTSERIESCHANGEDFAILUREMESSAGE;
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
            for (EnumEventStatus status : EnumEventStatus.values()) {
                if(status.status.equals(newStatus)) {
                    Set<Event> events = eventSeries.getEvents();

                    for (Event event : events) {
                        if (!event.getStatus().equals("abgeschlossen")) {
                            event.setStatus(status);
                            eventService.changeEvent(event);
                        }
                    }

                    eventSeriesRepository.save(eventSeries);
                    return LocalizedStringVariables.EVENTSERIESSTATUSCHANGESUCCESSMESSAGE;
                }
            }
            return LocalizedStringVariables.GIVENEVENTSTATUSNOTCORRECTMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.EVENTSERIESSTATUSCHANGEFAILURESMESSAGE;
        }
    }

    /**
     * A series of events is being deleted from the database.
     * @param eventSeriesId ID of the EventSeries which contains the series of events.
     * @return String about success or failure.
     */
    public String deleteEventSeries(long eventSeriesId) {
        EventSeries eventSeries = getEventSeriesById(eventSeriesId);
        try {
            Set<Event> events = eventSeries.getEvents();

            boolean deletionSuccessful = true;

            for (Event event : events) {
                EnumEventStatus status = event.getStatus();
                if (status.equals(EnumEventStatus.CANCELLED) || status.equals(EnumEventStatus.ACCOMPLISHED)) {
                    long eventId = event.getId();
                    this.eventService.deleteEvent(eventId);
                } else {
                    deletionSuccessful = false;
                }
            }
            if (deletionSuccessful) {
                this.eventSeriesRepository.deleteById(eventSeriesId);
                return LocalizedStringVariables.EVENTSERIESDELETEDSUCCESSMESSAGE;
            }
            else {
                return LocalizedStringVariables.EVENTSERIESDELETEDNOTALLMESSAGE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.EVENTSERIESDELETEDFAILUREMESSAGE;
        }
    }

    /**
     * Invites a user to a series of events by inviting him to the next event of the series.
     * @param eventSeriesId ID of the eventseries who contains the multiple events.
     * @param userMail Mail og the user who wille be invited.
     * @return String about success or failure.
     */
    public String inviteUserToEventSeries(long eventSeriesId, String userMail) {
        EventSeries eventSeries = getEventSeriesById(eventSeriesId);
        Set<Event> events = eventSeries.getEvents();

        LocalDate currentDate = LocalDate.now();

        Event nextEvent = null;
        for (Event event : events) {
            LocalDate startDate = event.getStartDate().toLocalDate();
            if (startDate.isAfter(currentDate) && (nextEvent == null || startDate.isBefore(nextEvent.getStartDate().toLocalDate()))) {
                nextEvent = event;
            }
            if (nextEvent != null && startDate.isAfter(nextEvent.getStartDate().toLocalDate())) {
                break;
            }
        }
        if (nextEvent != null) {
            return userInEventWithRoleService.inviteUserToFirstEventFromSeries(eventSeries, nextEvent.getId(), userMail);
        }
        else {
            return LocalizedStringVariables.EVENTSERIESNONEXTEVENTFOUNDMESSAGE;
        }
    }
}
