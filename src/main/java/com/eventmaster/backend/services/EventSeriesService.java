package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.EventSeriesRepository;
import com.eventmaster.backend.security.authentification.AuthenticationResponse;
import local.variables.LocalizedStringVariables;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final GroupService groupService;
    private final UserInGroupService userInGroupService;
    private final UserInEventWithRoleService userInEventWithRoleService;

    public EventSeriesService(EventSeriesRepository eventSeriesRepository,
                              EventService eventService,
                              GroupService groupService,
                              UserInGroupService userInGroupService,
                              @Lazy UserInEventWithRoleService userInEventWithRoleService) {
        this.eventSeriesRepository = eventSeriesRepository;
        this.eventService = eventService;
        this.groupService = groupService;
        this.userInGroupService = userInGroupService;
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
        try {
            eventSeriesRepository.save(eventSeries);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A series of events is being changed.
     * @param startEvent New event with the data for all other events and the last date of taking place.
     * @param eventSeries New eventSeries with info about time interval between the events.
     * @return String about success or failure.
     */
    public String changeEventSeries(Event startEvent, EventSeries eventSeries) {
        try {
            long eventSeriesId = eventSeries.getId();

            EventSeries oldEventSeries = getEventSeriesById(eventSeriesId);
            Set<Event> events = oldEventSeries.getEvents();

            for (Event event : events) {
                event.setName(startEvent.getName());
                event.setType(startEvent.getType());
                event.setDescription(startEvent.getDescription());
                event.setImage(startEvent.getImage());
                event.setLocation(startEvent.getLocation());
                event.setStartTime(startEvent.getStartTime());
                event.setEndTime(startEvent.getEndTime());
            }

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
                            eventService.changeEvent(event, null);
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
     * Changes the role of a person in all events of a series.
     * @param eventSeriesId ID of the eventseries.
     * @param userMail Mail of the user whose role gets changed.
     * @param toAttendee Boolean if role is changed to attendee or otherwise tutor.
     * @return String about success or failure.
     */
    public String changeRoleOfPersonInEventSeries(long eventSeriesId, String userMail, boolean toAttendee) {
        EventSeries eventSeries = getEventSeriesById(eventSeriesId);
        Set<Event> events = eventSeries.getEvents();
        try {
            for (Event event : events) {
                userInEventWithRoleService.changeRoleOfPersonInEvent(event.getId(), userMail, toAttendee);
            }
            return LocalizedStringVariables.CHANGEDROLEOFPERSONINEVENTSERIESSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.CHANGEDROLEOFPERSONINEVENTSERIESFAILUREMESSAGE;
        }
    }

    /**
     * Invites a user to a series of events by inviting him to the next event of the series.
     * @param eventSeriesId ID of the eventseries who contains the multiple events.
     * @param userMail Mail og the user who wille be invited.
     * @return String about success or failure.
     */
    public String inviteUserToEventSeries(long eventSeriesId, String userMail, boolean byGroup) {
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
            return userInEventWithRoleService.inviteUserToFirstEventFromSeries(eventSeries, nextEvent.getId(), userMail, byGroup);
        }
        else {
            return LocalizedStringVariables.EVENTSERIESNONEXTEVENTFOUNDMESSAGE;
        }
    }

    /**
     * Removes a user from an eventseries.
     * @param eventSeriesId ID of the eventseries.
     * @param userMail Mail of the user who will be removed.
     * @return String about success or failure.
     */
    public String removeUserFromEventSeries(long eventSeriesId, String userMail) {
        EventSeries eventSeries = getEventSeriesById(eventSeriesId);
        LocalDate currentDate = LocalDate.now();

        Set<Event> events = eventSeries.getEvents();
        try {
            for (Event event : events) {
                LocalDate startDate = event.getStartDate().toLocalDate();
                if (startDate.isAfter(currentDate)) {
                    userInEventWithRoleService.removeUserFromEvent(event.getId(), userMail);
                }
            }
            saveEventSeries(eventSeries);
            return LocalizedStringVariables.REMOVEUSERFROMEVENTSERIESSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.REMOVEUSERFROMEVENTSERIESFAILUREMESSAGE;
        }
    }

    /**
     * Invites a group to a series of events.
     * @param eventSeriesId ID of the eventseries.
     * @param groupId ID of the group which will be invited.
     * @return String about success or failure.
     */
    public String inviteGroupToEventSeries(long eventSeriesId, long groupId) {
        List<User> usersOfGroup = userInGroupService.getUsersOfGroup(groupId);

        try {
            for (User user : usersOfGroup) {
                inviteUserToEventSeries(eventSeriesId, user.getEmailAdress(), true);
            }
            return LocalizedStringVariables.INVITEDGROUPTOEVENTSERIESSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.INVITEDGROUPTOEVENTSERIESFAILUREMESSAGE;
        }
    }

    /**
     * Removes a group from the upcoming events of a series.
     * @param eventSeriesId ID of the eventseries.
     * @param groupId ID of the group which will be removed.
     * @param reason Reason why the group will be removed.
     * @return String about success or failure.
     */
    public String removeGroupFromEventSeries(long eventSeriesId, long groupId, String reason) {
        EventSeries eventSeries = getEventSeriesById(eventSeriesId);
        List<User> usersOfGroup = userInGroupService.getUsersOfGroup(groupId);
        LocalDate currentDate = LocalDate.now();

        Set<Event> events = eventSeries.getEvents();
        try {
            for (Event event : events) {
                LocalDate startDate = event.getStartDate().toLocalDate();
                if (startDate.isAfter(currentDate)) {
                    userInEventWithRoleService.removeUsersOfGroupFromEvent(event.getId(), usersOfGroup, reason);
                }
            }
            saveEventSeries(eventSeries);
            return LocalizedStringVariables.REMOVEGROUPFROMEVENTSERIESSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.REMOVEGROUPFROMEVENTSERIESFAILUREMESSAGE;
        }
    }

    /**
     * Sets the states of all upcoming events of the series to "cancelled".
     * @param eventSeriesId ID of the eventseries.
     * @return String about success or failure.
     */
    public String cancelEventSeries(long eventSeriesId) {
        EventSeries eventSeries = getEventSeriesById(eventSeriesId);
        LocalDate currentDate = LocalDate.now();

        Set<Event> events = eventSeries.getEvents();

        try {
            for (Event event : events) {
                LocalDate startDate = event.getStartDate().toLocalDate();
                if (startDate.isAfter(currentDate)) {
                    event.setStatus(EnumEventStatus.CANCELLED);
                    eventService.saveEvent(event);
                }
            }
            saveEventSeries(eventSeries);
            return LocalizedStringVariables.EVENTSERIESCANCELLEDSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.EVENTSERIESCANCELLEDFAILUREMESSAGE;
        }
    }
}
