package com.eventmaster.backend.controller;

import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.entities.EventSeries;
import com.eventmaster.backend.entities.MessageResponse;
import com.eventmaster.backend.entities.Preset;
import com.eventmaster.backend.serviceswithouttoken.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
    private final UserInOrgaWithRoleService userInOrgaWithRoleService;
    private final GroupInEventService groupInEventService;

    public OrganizerController(EventService eventService,
                               EventSeriesService eventSeriesService,
                               OrganisationService organisationService,
                               PresetService presetService,
                               UserInEventWithRoleService userInEventWithRoleService,
                               UserInOrgaWithRoleService userInOrgaWithRoleService,
                               GroupInEventService groupInEventService) {
        this.eventService = eventService;
        this.eventSeriesService = eventSeriesService;
        this.organisationService = organisationService;
        this.presetService = presetService;
        this.userInEventWithRoleService = userInEventWithRoleService;
        this.userInOrgaWithRoleService = userInOrgaWithRoleService;
        this.groupInEventService = groupInEventService;
    }

    //region Event

   /**
     * Endpoint to get all managing events in a organisation.
     * @param userMail Mail of the organizer.
     * @param orgaId ID of the organisation.
     * @return List of events.
     */
    @PostMapping("/orga/{orgaId}/event/managing/get/{userMail}")
    public ResponseEntity<List<Event>> getManagingEventsInOrga(@PathVariable String userMail,
                                                               @PathVariable long orgaId) {
        return ResponseEntity.ok(userInEventWithRoleService.getManagingEvents(userMail, orgaId));
    }

    /**
     * Endpoint to create an event.
     * @param event Event which will be saved in the database.
     * @param userMail Mail of user who created event and becomes organizer.
     * @param orgaId Organisation which will contain the event.
     * @return String about success or failure.
     */
    @PostMapping("/event/create/{userMail}/{orgaId}")
    public ResponseEntity<MessageResponse> createEvent(@RequestBody Event event,
                                                       @PathVariable String userMail,
                                                       @PathVariable long orgaId) {
        return ResponseEntity.ok(userInEventWithRoleService.createEventWithOrganizer(event, userMail, orgaId));
    }

    /**
     * Endpoint to change an event.
     * @param event New Event with the new data.
     * @return String about success or failure.
     */
    @PostMapping("/event/change")
    public ResponseEntity<MessageResponse> changeEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.changeEvent(event));
    }

    /**
     * Endpoint to change the status of an event.
     * @param eventId ID of the event where the status will be changed.
     * @param status New status of the event.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/status/change")
    public ResponseEntity<MessageResponse> changeStatusOfEvent(@PathVariable long eventId,
                                                      @RequestBody String status) {
        return ResponseEntity.ok(eventService.changeStatusOfEvent(eventId, status));
    }

    /**
     * Endpoint to delete an event.
     * @param eventId ID of the event which will be deleted.
     * @return String about success or failure.
     */
    @PostMapping("/event/delete/{eventId}")
    public ResponseEntity<MessageResponse> deleteEvent(@PathVariable long eventId) {
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

    /**
     * Endpoint to invite a user to an event.
     * @param eventId ID of the event.
     * @param userMail Mail of the user who will be invited.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/user/invite")
    public ResponseEntity<String> inviteUserToEvent(@PathVariable long eventId,
                                                    @RequestBody String userMail) {
        return ResponseEntity.ok(userInEventWithRoleService.inviteUserToEvent(eventId, userMail, true));
    }

    /**
     * Endpoint to invite a tutor to an event.
     * @param eventId ID of the event.
     * @param userMail Mail of the tutor.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/tutor/invite")
    public ResponseEntity<String> inviteTutorToEvent(@PathVariable long eventId,
                                                     @RequestBody String userMail) {
        return ResponseEntity.ok(userInEventWithRoleService.inviteTutorToEvent(eventId, userMail));
    }

    /**
     * Endpoint to invite a group of users to an event.
     * @param eventId ID of the event.
     * @param groupId ID of the group which will be invited.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/group/invite")
    public ResponseEntity<String> inviteGroupToEvent(@PathVariable long eventId,
                                                     @RequestBody long groupId) {
        return ResponseEntity.ok(groupInEventService.inviteGroupToEvent(eventId, groupId));
    }

    /**
     * Endpoint to remove a user from an event.
     * @param eventId ID of the event.
     * @param userMail Mail of the user who will be removed.
     * @param reason Reason why user will be removed.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/attendee/remove")
    public ResponseEntity<String> removeUserFromEvent(@PathVariable long eventId,
                                                      @RequestBody String userMail,
                                                      @RequestParam String reason) {
        return ResponseEntity.ok(userInEventWithRoleService.removeUserFromEvent(eventId, userMail, reason));
    }

    /**
     * Endpoint to remove a group from an event.
     * @param eventId ID of the event.
     * @param groupId ID of the group which will be removed.
     * @param reason Reason why the group will be removed.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/group/remove")
    public ResponseEntity<String> removeGroupFromEvent(@PathVariable long eventId,
                                                       @RequestBody long groupId,
                                                       @RequestParam String reason) {
        return ResponseEntity.ok(groupInEventService.removeGroupFromEvent(eventId, groupId, reason));
    }

    /**
     * Endpoint to set the status of an event to "cancelled".
     * @param eventId ID of the event.
     * @param reason Reason why the event is cancelled.
     * @return String about success or failure.
     */
    @PostMapping("/event/{eventId}/cancel")
    public ResponseEntity<MessageResponse> cancelEvent(@PathVariable long eventId,
                                              @RequestBody String reason) {
        return ResponseEntity.ok(eventService.cancelEvent(eventId, reason));
    }

    //endregion



    //region EventSeries

    /**
     * Endpoint to create a series of events.
     * @param params Includes the startEvent and the eventseries data.
     * @param userMail Mail of user who created the events and becomes organizers.
     * @param orgaId ID of the organisation.
     * @return String about success or failure.
     */
    @PostMapping(value = "/event-series/create/{userMail}/{orgaId}", produces = "application/json")
    public ResponseEntity<String> createEventSeries(@RequestBody JsonNode params,
                                                    @PathVariable String userMail,
                                                    @PathVariable long orgaId) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode startEventJson = params.get("startEvent");
        Event startEvent = mapper.convertValue(startEventJson, Event.class);

        JsonNode eventSeriesJson = params.get("eventSeries");
        EventSeries eventSeries = mapper.convertValue(eventSeriesJson, EventSeries.class);

        ObjectNode response = mapper.createObjectNode();
        response.put("message", userInEventWithRoleService.createEventSeriesWithOrganizer(startEvent, eventSeries, userMail, orgaId));
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response.toString());
    }

    /**
     * Endpoint to change a series of events.
     * @param eventSeries New EventSeries with information about time interval between events.
     * @param startEvent New start event of the series.
     * @return String about success or failure.
     */
    @PostMapping("/event-series/change")
    public ResponseEntity<String> changeEventSeries(@RequestParam EventSeries eventSeries,
                                                    @RequestParam Event startEvent) {
        return ResponseEntity.ok(eventSeriesService.changeEventSeries(startEvent, eventSeries));
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
     * Endpoint to change an attendee of an eventseries to a tutor of the eventseries.
     * @param eventSeriesId ID of the eventseries.
     * @param userMail Mail of user who gets Attendee.
     * @return String about success or failure.
     */
    @PostMapping("/event-series/{eventSeriesId}/attendee/set-tutor")
    public ResponseEntity<String> changeAttendeeToTutorInEventSeries(@PathVariable long eventSeriesId,
                                                                     @RequestBody String userMail) {
        return ResponseEntity.ok(eventSeriesService.changeRoleOfPersonInEventSeries(eventSeriesId, userMail, false));
    }

    /**
     * Endpoint to change a tutor of an eventseries to an attendee of the eventseries.
     * @param eventSeriesId ID of the eventseries.
     * @param userMail Mail of user who gets Attendee.
     * @return String about success or failure.
     */
    @PostMapping("/event-series/{eventSeriesId}/attendee/set-attendee")
    public ResponseEntity<String> changeTutorToAttendeeInEventSeries(@PathVariable long eventSeriesId,
                                                                     @RequestBody String userMail) {
        return ResponseEntity.ok(eventSeriesService.changeRoleOfPersonInEventSeries(eventSeriesId, userMail, true));
    }

    /**
     * Endpoint to invite a user to a series of events.
     * @param eventSeriesId ID of the eventseries.
     * @param userMail Mail of the user who will be invited.
     * @return String about success or failure.
     */
    @PostMapping("/event-series/{eventSeriesId}/user/invite")
    public ResponseEntity<String> inviteUserToEventSeries(@PathVariable long eventSeriesId,
                                                          @RequestBody String userMail) {
        return ResponseEntity.ok(eventSeriesService.inviteUserToEventSeries(eventSeriesId, userMail, false));
    }

    /**
     * Endpoint to invite a group to a series of events.
     * @param eventSeriesId ID of the eventseries.
     * @param groupId ID of the group which will be invited.
     * @return String about success or failure.
     */
    @PostMapping("/event-series/{eventSeriesId}/group/invite")
    public ResponseEntity<String> inviteGroupToEventSeries(@PathVariable long eventSeriesId,
                                                           @RequestBody long groupId) {
        return ResponseEntity.ok(eventSeriesService.inviteGroupToEventSeries(eventSeriesId, groupId));
    }

    /**
     * Endpoint to remove a user from an eventseries.
     * @param eventSeriesId ID of the eventseries.
     * @param userMail Mail of the user who will be removed.
     * @param reason Reason why the user will be removed.
     * @return String about success or failure.
     */
    @PostMapping("/event-series/{eventSeriesId}/user/remove")
    public ResponseEntity<String> removeUserFromEventSeries(@PathVariable long eventSeriesId,
                                                            @RequestBody String userMail,
                                                            @RequestParam String reason) {
        return ResponseEntity.ok(eventSeriesService.removeUserFromEventSeries(eventSeriesId, userMail, reason));
    }

    /**
     * Endpoint to remove a group from an eventseries.
     * @param eventSeriesId ID of the eventseries.
     * @param groupId ID of the group which will be removed from eventseries.
     * @param reason Reason why the group will be removed.
     * @return String about success or failure.
     */
    @PostMapping("/event-series/{eventSeriesId}/group/remove")
    public ResponseEntity<String> removeGroupFromEventSeries(@PathVariable long eventSeriesId,
                                                             @RequestBody long groupId,
                                                             @RequestParam String reason) {
        return ResponseEntity.ok(eventSeriesService.removeGroupFromEventSeries(eventSeriesId, groupId, reason));
    }

    /**
     * Endpoint to cancel an eventseries.
     * @param eventSeriesId ID of the eventseries which will be cancelled.
     * @return String about success or failure.
     */
    @PostMapping("/event-series/{eventSeriesId}/cancel")
    public ResponseEntity<String> cancelEventSeries(@PathVariable long eventSeriesId) {
        return ResponseEntity.ok(eventSeriesService.cancelEventSeries(eventSeriesId));
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

    //region Organisation

    /**
     * Endpoint to invite a user to an organisation.
     * @param orgaId ID of the organisation.
     * @param userMail Mail of the user who will be invited.
     * @return String about success or failure.
     */
    @PostMapping("/organisation/{orgaId}/user/invite")
    public ResponseEntity<String> inviteUserToOrganisation(@PathVariable long orgaId,
                                                           @RequestBody String userMail) {
        return ResponseEntity.ok(userInOrgaWithRoleService.inviteUserToOrganisation(orgaId, userMail));
    }

    //endregion
}
