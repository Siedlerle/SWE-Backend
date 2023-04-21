package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.EventSeries;
import com.eventmaster.backend.entities.Preset;
import com.eventmaster.backend.serviceswithouttoken.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private EventService eventService;
    @Autowired
    private EventSeriesService eventSeriesService;
    @Autowired
    private OrganisationService organisationService;
    @Autowired
    private PresetService presetService;
    @Autowired
    private UserInEventWithRoleService userInEventWithRoleService;

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
    public ResponseEntity<String> changeEventStatus(@PathVariable long eventId,
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
}
