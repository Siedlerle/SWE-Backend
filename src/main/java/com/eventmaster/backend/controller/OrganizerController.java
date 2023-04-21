package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.EventSeries;
import com.eventmaster.backend.entities.Preset;
import com.eventmaster.backend.serviceswithouttoken.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A class which handles the HTTP-requests for organizer functions.
 *
 * @author Fabian Unger
 */
@RestController
@CrossOrigin
@RequestMapping("/organizer")
public class OrganizerController {

    private final EventService eventService;
    private final EventSeriesService eventSeriesService;
    private final OrganisationService organisationService;
    private final PresetService presetService;
    private final UserInEventWithRoleService userInEventWithRoleService;

    public OrganizerController(EventService eventService,
                               EventSeriesService eventSeriesService,
                               OrganisationService organisationService,
                               PresetService presetService,
                               UserInEventWithRoleService userInEventWithRoleService) {
        this.eventService = eventService;
        this.eventSeriesService = eventSeriesService;
        this.organisationService = organisationService;
        this.presetService = presetService;
        this.userInEventWithRoleService = userInEventWithRoleService;
    }

    //region Event

    /**
     * Endpoint to create an event.
     * @param event Event which will be saved in the database.
     * @param authToken Token to identify user.
     * @return String about success or failure.
     */
    @PostMapping("/event/create")
    public ResponseEntity<String> createEvent(@RequestBody Event event,
                                              @RequestParam String authToken) {
        long userId = 0;
        return ResponseEntity.ok(eventService.createEvent(event, userId));
    }

    /**
     * Endpoint to change an event.
     * @param event New Event with the new data.
     * @return String about success or failure.
     */
    @PostMapping("/event/change")
    public ResponseEntity<String> changeEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.changeEvent(event));
    }

    /**
     * Endpoint to change the status of an event.
     * @param eventId ID of the event where the status will be changed.
     * @param status New status of the event.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/status/change")
    public ResponseEntity<String> changeStatusOfEvent(@PathVariable long eventId,
                                                      @RequestBody String status) {
        return ResponseEntity.ok(eventService.changeStatusOfEvent(eventId, status));
    }

    /**
     * Endpoint to delete an event.
     * @param eventId ID of the event which will be deleted.
     * @return String about success or failure.
     */
    @PostMapping("/event/delete/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable long eventId) {
        return ResponseEntity.ok(eventService.deleteEvent(eventId));
    }

    /**
     * Endpoint to change an attendee to a tutor in an event.
     * @param eventId ID of the event.
     * @param userMail Mail address of the user whom role will be changed.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/attendee/set-tutor")
    public ResponseEntity<String> changeAttendeeToTutorOfEvent(@PathVariable long eventId,
                                                               @RequestBody String userMail) {
        return ResponseEntity.ok(userInEventWithRoleService.changeRoleOfPersonInEvent(eventId, userMail, false));
    }

    /**
     * Endpoint to change a tutor to an attendee in an event.
     * @param eventId ID of the event.
     * @param userMail Mail address of the user whom role will be changed.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/attendee/set-attendee")
    public ResponseEntity<String> changeTutorToAttendeeInEvent(@PathVariable long eventId,
                                                               @RequestBody String userMail) {
        return ResponseEntity.ok(userInEventWithRoleService.changeRoleOfPersonInEvent(eventId, userMail, true));
    }
    //endregion



    //region EventSeries

    /**
     * Endpoint to create a series of events.
     * @param eventSeries EventSeries with information about time interval between events.
     * @param lastEvent Last event of the event series with event data.
     * @return String about success or failure.
     */
    @PostMapping("/event-series/create")
    public ResponseEntity<String> createEventSeries(@RequestParam EventSeries eventSeries,
                                                    @RequestParam Event lastEvent) {
        return ResponseEntity.ok(eventSeriesService.createEventSeries(lastEvent, eventSeries));
    }

    /**
     * Endpoint to change a series of events.
     * @param eventSeries New EventSeries with information about time interval between events.
     * @param lastEvent New last event of the series.
     * @return String about success or failure.
     */
    @PostMapping("/event-series/change")
    public ResponseEntity<String> changeEventSeries(@RequestParam EventSeries eventSeries,
                                                    @RequestParam Event lastEvent) {
        return ResponseEntity.ok(eventSeriesService.changeEventSeries(lastEvent, eventSeries));
    }

    /**
     * Endpoint to change the status of the events belonging to the EventSeries.
     * @param eventSeriesId ID of the EventSeries containing the events.
     * @param status New status for the events.
     * @return String about success or failure.
     */
    @PostMapping("/event-series/{eventSeriesId}/status/change")
    public ResponseEntity<String> changeStatusOfEventSeries(@PathVariable long eventSeriesId,
                                                            @RequestBody String status) {
        return ResponseEntity.ok(eventSeriesService.changeStatusOfEventSeries(eventSeriesId, status));
    }

    /**
     * Endpoint to delete a series of events.
     * @param eventSeriesId ID of the eventseries which will be deleted.
     * @return String about success or failure.
     */
    @PostMapping("/event-series/delete/{eventSeriesId}")
    public ResponseEntity<String> deleteEventSeries(@PathVariable long eventSeriesId) {
        return ResponseEntity.ok(eventSeriesService.deleteEventSeries(eventSeriesId));
    }

    //endregion

    //region Presets

    /**
     * Endpoint to get all presets from an organisation.
     * @param organisationId ID of the organisation which contains the presets.
     * @return List of presets.
     */
    @PostMapping("/preset/get-from-orga")
    public ResponseEntity<List<Preset>> getPresetsForOrganisation(@RequestBody long organisationId) {
        return ResponseEntity.ok(presetService.getPresetsForOrganisation(organisationId));
    }

    /**
     * Endpoint to create a preset and add it to his organisation.
     * @param orgaId ID of the organisation which will contain the preset.
     * @param preset Preset which will be added to the database and the organisation.
     * @return String about success or failure.
     */
    @PostMapping("/preset/create/{orgaId}")
    public ResponseEntity<String> createPreset(@PathVariable long orgaId,
                                               @RequestBody Preset preset) {
        return ResponseEntity.ok(presetService.createPreset(orgaId, preset));
    }

    /**
     * Endpoint to change a preset.
     * @param preset New preset with the new information.
     * @return String about success or failure.
     */
    @PostMapping("/preset/change")
    public ResponseEntity<String> changePreset(@RequestBody Preset preset) {
        return ResponseEntity.ok(presetService.changePreset(preset));
    }

    /**
     * Endpoint to delete a preset.
     * @param presetId ID of the preset which will be deleted.
     * @return String about success or failure.
     */
    @PostMapping("/preset/delete/{presetId}")
    public ResponseEntity<String> deletePreset(@PathVariable long presetId) {
        return ResponseEntity.ok(presetService.deletePreset(presetId));
    }

    //endregion
}
