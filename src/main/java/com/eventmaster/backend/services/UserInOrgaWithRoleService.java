package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.UserInEventWithRoleRepository;
import com.eventmaster.backend.repositories.UserInOrgaWithRoleRepository;
import local.variables.LocalizedStringVariables;
import org.aspectj.bridge.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of the connection between users and organisations witch specific roles.
 *
 * @author Fabian Eilber
 * @author Fabian Unger
 */
@Service
public class UserInOrgaWithRoleService {
    private final UserInOrgaWithRoleRepository userInOrgaWithRoleRepository;
    private final UserService userService;
    private final OrganisationService organisationService;
    private final OrgaRoleService orgaRoleService;
    private final EventService eventService;
    private final UserInEventWithRoleService userInEventWithRoleService;

    public UserInOrgaWithRoleService(
            UserInOrgaWithRoleRepository userInOrgaWithRoleRepository,
            UserService userService,
            OrganisationService organisationService,
            OrgaRoleService orgaRoleService,
            EventService eventService,
            UserInEventWithRoleService userInEventWithRoleService) {
        this.userInOrgaWithRoleRepository = userInOrgaWithRoleRepository;
        this.userService = userService;
        this.organisationService = organisationService;
        this.orgaRoleService = orgaRoleService;
        this.eventService = eventService;
        this.userInEventWithRoleService = userInEventWithRoleService;
    }


    /**
     * Retrieve the organisations that the user is part of.
     * @param userMail Mail of the corresponding user.
     * @return List of Organisations.
     */
    public List<Organisation> getOrgasForUser(String userMail) {
        /*User user = userService.getUserByMail(userMail);
        List<Organisation> organisations = userInOrgaWithRoleRepository
                .findByUser(user)
                .stream()
                .filter(uiOwR -> uiOwR.getOrgaRole().getRole().ordinal() >= 2)//Checks if the tested user is user or above in the organisation
                .map(UserInOrgaWithRole::getOrganisation)
                .toList();*/
        try {
            User user = userService.getUserByMail(userMail);
            List <UserInOrgaWithRole> userInOrgaWithRoles = userInOrgaWithRoleRepository.findByUser(user);

            List <Organisation> organisationsForUser = new ArrayList<Organisation>();

            for(UserInOrgaWithRole orgasForUser: userInOrgaWithRoles){
                if(!orgasForUser.getOrgaRole().getRole().equals(EnumOrgaRole.INVITED) && !orgasForUser.getOrgaRole().getRole().equals(EnumOrgaRole.REQUESTED)){
                    organisationsForUser.add(orgasForUser.getOrganisation());
                }
            }

            return organisationsForUser;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieve the role of a user inside an organisation
     * @param organisationId Id of the corresponding organisation
     * @param emailAdress Email of the corresponding user
     * @return Role of the user in the Organsation
     */
    public OrgaRole getRoleInOrganisation(long organisationId, String emailAdress){
        try {
            User user = userService.getUserByMail(emailAdress);

            UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(),organisationId);

            return userInOrgaWithRole.getOrgaRole();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Retrieves all visible events for a user in an organisation
     * @param organisationId Id of the corresponding organisation
     * @param userMail Mail of the user.
     * @return List of events
     */
    public List<Event> getAllVisibleEventsOfOrganisationForUser(long organisationId, String userMail){
        List<Event> organisationEvents = eventService.getEventsOfOrganisation(organisationId);

        Map<Long, Event> nextEventInSeries = new HashMap<>();
        Set<Event> nonSeriesEvents = new HashSet<>();
        for (Event event : organisationEvents) {

            if (event.getEventSeries() != null) {
                long seriesId = event.getEventSeries().getId();
                Event nextEvent = nextEventInSeries.get(seriesId);
                if ((nextEvent == null
                        || event.getStartDate().toLocalDate().isBefore(nextEvent.getStartDate().toLocalDate()))
                        && event.getStartDate().toLocalDate().isAfter(LocalDate.now())) {
                    nextEventInSeries.put(seriesId, event);
                }
            } else {
                if (event.getStartDate().toLocalDate().isEqual(LocalDate.now())
                        || event.getStartDate().toLocalDate().isAfter(LocalDate.now())) {
                    nonSeriesEvents.add(event);
                }
            }
        }


        List<Event> filteredEvents = new ArrayList<>(nextEventInSeries.values());
        for (Event event : nonSeriesEvents) {
            filteredEvents.add(event);
        }

        filteredEvents.sort(Comparator.comparing(Event::getStartDate));

        List<Event> visibleEventsForUser = new ArrayList<>();

        try {
            for (Event organisationEvent : filteredEvents) {
                if (organisationEvent.getIsPublic()) {
                    if (!userInEventWithRoleService.isUserRegisteredToEvent(organisationEvent.getId(), userMail)) {
                        visibleEventsForUser.add(organisationEvent);
                    }
                }
            }
            return visibleEventsForUser;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all organizers of an organisation.
     * @param organisationId ID of organisation.
     * @return List of users who are organizer in organisation.
     */
    public List<User> getOrganizersOfOrganisation(long organisationId) {
        Organisation organisation = organisationService.getOrganisationById(organisationId);
        OrgaRole organizerRole = orgaRoleService.findByRole(EnumOrgaRole.ORGANIZER);

        List<UserInOrgaWithRole> userInOrgaWithRoleList = userInOrgaWithRoleRepository.findByOrganisation_Id(organisationId);

        List<User> organizers = new ArrayList<>();

        for (UserInOrgaWithRole userInOrgaWithRole: userInOrgaWithRoleList) {
            if (userInOrgaWithRole.getOrgaRole().equals(organizerRole)) {
                User organizer = userInOrgaWithRole.getUser();
                organizers.add(organizer);
            }
        }

        return organizers;
    }

    /**
     * Retrieves the registered events for a user in an organisation
     * @param organisationId Id of the corresponding organisation
     * @param emailAdress Email of the corresponding user
     * @return List of events
     */
    public List<Event> getRegisteredEventsForUserInOrganisation(long organisationId, String emailAdress){
        try {
            List<Event> userInEventWithRoles = userInEventWithRoleService.getRegisteredEventsForUser(emailAdress);

            List<Event> registeredEventsInOrga = new ArrayList<>();
            for (Event check : userInEventWithRoles) {
                if(check.getOrganisation().getId() == organisationId){
                    registeredEventsInOrga.add(check);
                }
            }
            registeredEventsInOrga.sort(Comparator.comparing(Event::getStartDate));
            return registeredEventsInOrga;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Sets a new userInOrgaWithRole
     * @param organisationId Id of the corresponding orga
     * @param userMail Mail of the corresponding user
     * @return successmessage
     */
    public MessageResponse requestJoin(long organisationId, String userMail){
        try {
            User user = userService.getUserByMail(userMail);
            Organisation organisation = organisationService.getOrganisationById(organisationId);
            OrgaRole requestRole = orgaRoleService.findByRole(EnumOrgaRole.REQUESTED);

            if(userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), organisationId) == null){
                UserInOrgaWithRole userInOrgaWithRole = new UserInOrgaWithRole();
                userInOrgaWithRole.setUser(user);
                userInOrgaWithRole.setOrganisation(organisation);
                userInOrgaWithRole.setOrgaRole(requestRole);

                userInOrgaWithRoleRepository.save(userInOrgaWithRole);
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.REQUESTORGAJOINSUCCESSMESSAGE)
                        .build();
            }
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.REQUESTORGAJOINALREADYEXISTSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.REQUESTORGAJOINFAILUREMESSAGE)
                    .build();
        }
    }

    public List<User> getRequestingUsers(long orgaId){

        try {
            List<UserInOrgaWithRole> userInOrgaWithRoles = userInOrgaWithRoleRepository.findByOrganisation_Id(orgaId);

            List<User> users = new ArrayList<>();

            for (UserInOrgaWithRole user: userInOrgaWithRoles) {
                if(user.getOrgaRole().getRole().equals(EnumOrgaRole.REQUESTED)){
                    users.add(user.getUser());
                }
            }
            return users;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The admin can accept a request to join his organisation
     * @param orgaId Id of the corresponding organisation
     * @param emailAdress Email of the corresponding user
     * @return success message
     */
    public MessageResponse acceptJoinRequest(long orgaId, String emailAdress){
        try {
            User user = userService.getUserByMail(emailAdress);
            UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), orgaId);
            OrgaRole accept = orgaRoleService.findByRole(EnumOrgaRole.USER);
            if(userInOrgaWithRole.getOrgaRole().getRole().equals(EnumOrgaRole.REQUESTED)){
                userInOrgaWithRole.setOrgaRole(accept);
                userInOrgaWithRoleRepository.save(userInOrgaWithRole);
                return MessageResponse.builder().message(LocalizedStringVariables.ACCEPTUSERJOINREQUESTSUCCESS).build();
            }
        }catch (Exception e){
            e.printStackTrace();
            return MessageResponse.builder().message(LocalizedStringVariables.ACCEPTUSERJOINREQUESTFAILURE).build();
        }
        return MessageResponse.builder().message(LocalizedStringVariables.ACCEPTUSERJOINREQUESTFAILURE).build();
    }

    /**
     * The admin can decline a request to join his organisation
     * @param orgaId Id of the corresponding organisation
     * @param emailAdress Email of the corresponding user
     * @return success message
     */
    public MessageResponse declineJoinRequest(long orgaId, String emailAdress){
        try {
            User user = userService.getUserByMail(emailAdress);
            UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), orgaId);
            if(userInOrgaWithRole.getOrgaRole().getRole().equals(EnumOrgaRole.REQUESTED)){

                user.removeUserInOrgaWithRole(userInOrgaWithRole);
                Organisation organisation = userInOrgaWithRole.getOrganisation();
                organisation.removeUserInOrgaWithRole(userInOrgaWithRole);
                OrgaRole role = userInOrgaWithRole.getOrgaRole();
                role.removeUserInOrgaWithRole(userInOrgaWithRole);

                userInOrgaWithRoleRepository.delete(userInOrgaWithRole);
                return MessageResponse.builder().message(LocalizedStringVariables.DECLINEUSERJOINREQUESTSUCCESS).build();
            }
        }catch (Exception e){
            e.printStackTrace();
            return MessageResponse.builder().message(LocalizedStringVariables.DECLINEUSERJOINREQUESTFAILURE).build();
        }
        return MessageResponse.builder().message(LocalizedStringVariables.DECLINEUSERJOINREQUESTFAILURE).build();
    }


    /**
     * The user can accept an invitation and is added to the organisation
     * @param organisationId Id of the corresponding orga
     * @param emailAdress Email of the corresponding user
     * @return success message
     */
    public MessageResponse acceptOrganisationInvite(long organisationId, String emailAdress){
        try {
            User user = userService.getUserByMail(emailAdress);

            UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), organisationId);

            userInOrgaWithRole.setOrganisation(organisationService.getOrganisationById(organisationId));
            userInOrgaWithRole.setUser(user);
            userInOrgaWithRole.setOrgaRole(orgaRoleService.findByRole(EnumOrgaRole.USER));

            userInOrgaWithRoleRepository.save(userInOrgaWithRole);

            return MessageResponse.builder()
                    .message(LocalizedStringVariables.ORGANISATIONIVITEACCPETEDSUCCESS)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.ORGANISATIONIVITEACCPETEDFAILURE)
                    .build();
        }
    }


    /**
     * The user can accept an invitation and is added to the organisation
     * @param organisationId Id of the corresponding orga
     * @param emailAdress Email of the corresponding user
     * @return success message
     */
    public MessageResponse declineOrganisationInvitation(long organisationId, String emailAdress){
        try {
            User user = userService.getUserByMail(emailAdress);
            Organisation orga = organisationService.getOrganisationById(organisationId);
            if(userInOrgaWithRoleRepository.existsByUser_IdAndOrganisation_Id(user.getId(), organisationId)){
                UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), organisationId);
                OrgaRole orgaRole = userInOrgaWithRole.getOrgaRole();

                user.removeUserInOrgaWithRole(userInOrgaWithRole);
                orga.removeUserInOrgaWithRole(userInOrgaWithRole);
                orgaRole.removeUserInOrgaWithRole(userInOrgaWithRole);

                userInOrgaWithRoleRepository.deleteById(userInOrgaWithRole.getId());

                return MessageResponse.builder()
                        .message(LocalizedStringVariables.ORGANISATIONINVITEDECLINESUCCESS)
                        .build();
            }else {
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.USERISNOTINORGANISATIONMESSAGE)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.ORGANISATIONINVITEDECLINEFAILURE)
                    .build();
        }
    }

    /**
     * If the user is part of the organisation he will be removed from her.
     * @param organisationId ID of the organisation.
     * @param userMail Mail of the user who will leave the organisatione.
     * @return String about success or failure.
     */
    public MessageResponse leaveOrganisation(long organisationId, String userMail){
        try {
            User user = userService.getUserByMail(userMail);
            Organisation organisation = organisationService.getOrganisationById(organisationId);

            if (userInOrgaWithRoleRepository.existsByUser_IdAndOrganisation_Id(user.getId(), organisationId)) {
                UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), organisationId);
                OrgaRole orgaRole = userInOrgaWithRole.getOrgaRole();

                List<Event> orgaEvents = eventService.getEventsOfOrganisation(organisationId);

                List<UserInEventWithRole> userEvents = userInEventWithRoleService.getEventsForUser(user.getId());

                for (Event event: orgaEvents) {
                    for (UserInEventWithRole userEventRole:userEvents) {
                        if(event.getId() == userEventRole.getEvent().getId()){
                            userInEventWithRoleService.deleteUserInEventWithRole(userEventRole);
                        }
                    }
                }


                user.removeUserInOrgaWithRole(userInOrgaWithRole);
                organisation.removeUserInOrgaWithRole(userInOrgaWithRole);
                orgaRole.removeUserInOrgaWithRole(userInOrgaWithRole);



                userInOrgaWithRoleRepository.deleteById(userInOrgaWithRole.getId());
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.LEAVEORGANISATIONSUCCESSMESSAGE)
                        .build();
            } else {
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.USERISNOTINORGANISATIONMESSAGE)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.LEAVEORGANISATIONFAILUREMESSAGE)
                    .build();
        }
    }


    /**
     * Removes a user from an organisation by deleting the UserInOrgaWithRole in the database.
     * @param organisationId ID of the organisation from which the user will be removed.
     * @param userMail Mail of the user who will be removed.
     * @return String about success of failure.
     */
    public MessageResponse removeUserFromOrganisation(long organisationId, String userMail) {
        try {

            User user = userService.getUserByMail(userMail);
            Organisation organisation = organisationService.getOrganisationById(organisationId);

            if (userInOrgaWithRoleRepository.existsByUser_IdAndOrganisation_Id(user.getId(), organisationId)) {
                UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), organisationId);
                OrgaRole orgaRole = userInOrgaWithRole.getOrgaRole();

                List<Event> orgaEvents = eventService.getEventsOfOrganisation(organisationId);

                List<UserInEventWithRole> userEvents = userInEventWithRoleService.getEventsForUser(user.getId());

                for (Event event: orgaEvents) {
                    for (UserInEventWithRole userEventRole:userEvents) {
                        if(event.getId() == userEventRole.getEvent().getId()){
                            userInEventWithRoleService.deleteUserInEventWithRole(userEventRole);
                        }
                    }
                }


                user.removeUserInOrgaWithRole(userInOrgaWithRole);
                organisation.removeUserInOrgaWithRole(userInOrgaWithRole);
                orgaRole.removeUserInOrgaWithRole(userInOrgaWithRole);



                userInOrgaWithRoleRepository.deleteById(userInOrgaWithRole.getId());
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.REMOVEDUSERFROMORGASUCCESSMESSAGE)
                        .build();
            } else {
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.USERISNOTINORGANISATIONMESSAGE)
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.REMOVEDUSERFROMORGAFAILUREMESSAGE)
                    .build();
        }
    }

    /**
     * Sets at an existing relation between user and organisation the role of the user to admin.
     * @param organisationId ID of the organisation where the user will be admin.
     * @param userMail Mail of the user who will be admin.
     * @return String about success or failure.
     */
    public MessageResponse setPersonAdmin(long organisationId, String userMail) {
        User user = userService.getUserByMail(userMail);
        OrgaRole newOrgaRole = orgaRoleService.findByRole(EnumOrgaRole.ADMIN);
        UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), organisationId);

        try {
            userInOrgaWithRole.setOrgaRole(newOrgaRole);
            userInOrgaWithRoleRepository.save(userInOrgaWithRole);
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.SETPERSONADMININORGASUCCESSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.SETPERSONADMININORGAFAILUREMESSAGE)
                    .build();
        }
    }

    /**
     * Sets at an existing relation between user and organisation the role of the user to organizer.
     * @param organisationId ID of the organisation where the user will be organizer.
     * @param userMail Mail of the user who will be organizer.
     * @return String about success or failure.
     */
    public MessageResponse setPersonOrganizer(long organisationId, String userMail) {
        User user = userService.getUserByMail(userMail);
        OrgaRole newOrgaRole = orgaRoleService.findByRole(EnumOrgaRole.ORGANIZER);
        UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), organisationId);

        try {
            userInOrgaWithRole.setOrgaRole(newOrgaRole);
            userInOrgaWithRoleRepository.save(userInOrgaWithRole);
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.SETPERSONORGANIZERINORGASUCCESSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.SETPERSONORGANIZERINORGAFAILUREMESSAGE)
                    .build();
        }
    }

    /**
     * Sets at an existing relation between user and organisation the role of the user to normal user.
     * @param organisationId ID of the organisation where the user will be a normal user.
     * @param userMail Mail of the user who will be a normal user.
     * @return String about success or failure.
     */
    public MessageResponse setPersonUser(long organisationId, String userMail) {
        User user = userService.getUserByMail(userMail);
        OrgaRole newOrgaRole = orgaRoleService.findByRole(EnumOrgaRole.USER);
        UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), organisationId);

        try {
            userInOrgaWithRole.setOrgaRole(newOrgaRole);
            userInOrgaWithRoleRepository.save(userInOrgaWithRole);
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.SETPERSONUSERINORGASUCCESSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.SETPERSONUSERINORGAFAILUREMESSAGE)
                    .build();
        }
    }

    /**
     * Invited a user to an organisation by setting the Role INVITED and sending the user an email.
     * @param organisationId ID of the organisation.
     * @param userMail Mail of the user who will be invited.
     * @return String about success or failure.
     */
    public MessageResponse inviteUserToOrganisation(long organisationId, String userMail) {
        Organisation organisation = organisationService.getOrganisationById(organisationId);
        User user = userService.getUserByMail(userMail);
        OrgaRole inviteRole = orgaRoleService.findByRole(EnumOrgaRole.INVITED);

        if (userInOrgaWithRoleRepository.existsByUser_IdAndOrganisation_Id(user.getId(), organisationId)) {
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.INVITEDUSERALREADYPARTOFORGANISATIONMESSAGE)
                    .build();
        } else {

            try {
                UserInOrgaWithRole userInOrgaWithRole = new UserInOrgaWithRole();
                userInOrgaWithRole.setOrganisation(organisation);
                userInOrgaWithRole.setUser(user);
                userInOrgaWithRole.setOrgaRole(inviteRole);
                userInOrgaWithRoleRepository.save(userInOrgaWithRole);
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.INVITEUSERTOORGANISATIONSUCCESSMESSAGE)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                return MessageResponse.builder()
                        .message(LocalizedStringVariables.INVITEUSERTOORGANISATIONFAILUREMESSAGE)
                        .build();

            }
        }
    }

    /**
     * Gets all organisations where a user is invited to.
     * @param userMail Mail of the user.
     * @return List of organisations.
     */
    public List<Organisation> getOrganisationInvitationsForUser(String userMail) {
        User user = userService.getUserByMail(userMail);

        List<Organisation> invitations = new ArrayList<>();

        OrgaRole invitedRole = orgaRoleService.findByRole(EnumOrgaRole.INVITED);

        try {
            List<UserInOrgaWithRole> userInOrgaWithRoleList = userInOrgaWithRoleRepository.findByUser(user);

            for (UserInOrgaWithRole userInOrgaWithRole : userInOrgaWithRoleList) {
                if (userInOrgaWithRole.getOrgaRole().equals(invitedRole)) {
                    Organisation organisation = userInOrgaWithRole.getOrganisation();
                    invitations.add(organisation);
                }
            }
            return invitations;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets all unaffiliated users of organisation for this event.
     * @param event Event to be checked.
     * @return List of users that aren't affiliated.
     */
    public List<User> getUnaffiliatedUsersForEvent(Event event) {
        long eventId = event.getId();
        List<User> allUsersInOrga = userInOrgaWithRoleRepository
                .findByOrganisation_Id(event.getOrganisation().getId())
                .stream()
                .filter(userInOrgaWithRole -> {
                    EnumOrgaRole role = userInOrgaWithRole.getOrgaRole().getRole()
                    ;return role == EnumOrgaRole.USER || role == EnumOrgaRole.ORGANIZER || role == EnumOrgaRole.ADMIN;
                })
                .map(UserInOrgaWithRole::getUser).toList();
        List<User> affiliated = userInEventWithRoleService
                .findByEventId(eventId)
                .stream()
                .map(UserInEventWithRole::getUser)
                .toList();
        List<User> unaffiliatedUsers = allUsersInOrga
                .stream()
                .filter(user -> !affiliated.contains(user))
                .toList();
        return unaffiliatedUsers;
    }

    /**
     * Gets all users of organisation(Not invited or requested once)
     * @param orgaId Id of the corresponding organisation
     * @return List of users
     */
    public List<User> getAllUsersInOrga(long orgaId){
            List<User> allUsersInOrga = userInOrgaWithRoleRepository
                .findByOrganisation_Id(orgaId)
                .stream()
                .filter(userInOrgaWithRole -> {
                    EnumOrgaRole role = userInOrgaWithRole.getOrgaRole().getRole()
                    ;return role == EnumOrgaRole.USER || role == EnumOrgaRole.ORGANIZER || role == EnumOrgaRole.ADMIN;
                })
                .map(UserInOrgaWithRole::getUser).toList();
            return allUsersInOrga;
    }
    //Sys-Admin
    /**
     * Sets a user Admin in an organisation.
     * @param organisationId Organisation.
     * @param adminEmail Person to be added as an admin
     * @return Failure or success.
     */
    public String setAdminForOrga(long organisationId, String adminEmail){
        UserInOrgaWithRole adminInOrga = new UserInOrgaWithRole();
        adminInOrga.setOrgaRole(orgaRoleService.findByRole(EnumOrgaRole.ADMIN));
        adminInOrga.setOrganisation(organisationService.getOrganisationById(organisationId));
        adminInOrga.setUser(userService.getUserByMail(adminEmail));
        try {
            userInOrgaWithRoleRepository.save(adminInOrga);
            return LocalizedStringVariables.SETPERSONADMININORGASUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.SETPERSONADMININORGAFAILUREMESSAGE;
        }
    }
}
