package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.UserInOrgaWithRoleRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
     * Retrieve information about orgas user is a part of
     * @param userMail Mail of the corresponding user
     * @return List of organisation objects
     */
    public List<Organisation> getOrgaForUser(String userMail) {
        try {
            User user = userService.getUserByMail(userMail);
            List <UserInOrgaWithRole> userInOrgaWithRoles = userInOrgaWithRoleRepository.findByUser(user);

            List <Organisation> organisationsForUser = new ArrayList<Organisation>();

            for(UserInOrgaWithRole orgasForUser: userInOrgaWithRoles){
                organisationsForUser.add(orgasForUser.getOrganisation());
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
        List<Event> visibleEventsForUser = new ArrayList<>();
        try {
            for (Event organisationEvent : organisationEvents) {
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
     * Retrieves the registered events for a user in an organisation
     * @param organisationId Id of the corresponding organisation
     * @param emailAdress Email of the corresponding user
     * @return List of events
     */
    public List<Event> getRegisteredEventsForUserInOrganisation(long organisationId, String emailAdress){
        try {
            User user = userService.getUserByMail(emailAdress);

            List<Event> userInEventWithRoles = userInEventWithRoleService.getRegisteredEventsForUser(emailAdress);

            List<Event> registeredEventsInOrga = new ArrayList<>();
            for (Event check : userInEventWithRoles) {
                if(check.getOrganisation().getId() == organisationId){
                    registeredEventsInOrga.add(check);
                }
            }

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
    public String requestJoin(long organisationId, String userMail){
        User user = userService.getUserByMail(userMail);
        Organisation organisation = organisationService.getOrganisationById(organisationId);
        OrgaRole requestRole = orgaRoleService.findByRole(EnumOrgaRole.REQUESTED);

        UserInOrgaWithRole userInOrgaWithRole = new UserInOrgaWithRole();
        userInOrgaWithRole.setUser(user);
        userInOrgaWithRole.setOrganisation(organisation);
        userInOrgaWithRole.setOrgaRole(requestRole);
        try {
            userInOrgaWithRoleRepository.save(userInOrgaWithRole);
            return LocalizedStringVariables.REQUESTORGAJOINSUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.REQUESTORGAJOINFAILUREMESSAGE;
        }
    }


    /**
     * The user can accept an invitation and is added to the organisation
     * @param organisationId Id of the corresponding orga
     * @param emailAdress Email of the corresponding user
     * @return success message
     */
    public String acceptOrganisationInvite(long organisationId, String emailAdress){
        try {
            User user = userService.getUserByMail(emailAdress);

            UserInOrgaWithRole userInOrgaWithRole = new UserInOrgaWithRole();
            userInOrgaWithRole.setOrganisation(organisationService.getOrganisationById(organisationId));
            userInOrgaWithRole.setUser(user);
            userInOrgaWithRole.setOrgaRole(orgaRoleService.findByRole(EnumOrgaRole.USER));

            userInOrgaWithRoleRepository.save(userInOrgaWithRole);

            return LocalizedStringVariables.ORGANISATIONIVITEACCPETEDSUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.ORGANISATIONIVITEACCPETEDFAILURE;
        }
    }


    /**
     * If the user is part of the organisation he will be removed from her.
     * @param organisationId ID of the organisation.
     * @param userMail Mail of the user who will leave the organisatione.
     * @param reason Reason why he leaves the organisation.
     * @return String about success or failure.
     */
    public String leaveOrganisation(long organisationId, String userMail, String reason){
        try {
            User user = userService.getUserByMail(userMail);
            //Todo reason per mail an den admin o.ä. der Organisation

            UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), organisationId);

            if(userInOrgaWithRole == null) {
                return LocalizedStringVariables.USERISNOTINORGANISATIONMESSAGE;
            } else {
                //Todo Löschen ist nicht funktional
                userInOrgaWithRoleRepository.delete(userInOrgaWithRole);
                return LocalizedStringVariables.LEAVEORGANISATIONSUCCESSMESSAGE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.LEAVEORGANISATIONFAILUREMESSAGE;
        }
    }


    /**
     * Removes a user from an organisation by deleting the UserInOrgaWithRole in the database.
     * @param organisationId ID of the organisation from which the user will be removed.
     * @param userMail Mail of the user who will be removed.
     * @return String about success of failure.
     */
    public String removeUserFromOrganisation(long organisationId, String userMail) {
        try {
            User user = userService.getUserByMail(userMail);
            Organisation organisation = organisationService.getOrganisationById(organisationId);
            List <UserInOrgaWithRole> userInOrgaWithRoles = userInOrgaWithRoleRepository.findByUser(user);

            for(UserInOrgaWithRole userInOrgaWithRole : userInOrgaWithRoles) {
                if (userInOrgaWithRole.getOrganisation().getId() == organisation.getId())
                {
                    userInOrgaWithRoleRepository.delete(userInOrgaWithRole);
                    return LocalizedStringVariables.REMOVEDUSERFROMORGASUCCESSMESSAGE;
                }
            }

            return LocalizedStringVariables.USERISNOTINORGANISATIONMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.REMOVEDUSERFROMORGAFAILUREMESSAGE;
        }
    }

    /**
     * Sets at an existing relation between user and organisation the role of the user to admin.
     * @param organisationId ID of the organisation where the user will be admin.
     * @param userMail Mail of the user who will be admin.
     * @return String about success or failure.
     */
    public String setPersonAdmin(long organisationId, String userMail) {
        User user = userService.getUserByMail(userMail);
        OrgaRole newOrgaRole = orgaRoleService.findByRole(EnumOrgaRole.ADMIN);
        UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), organisationId);

        try {
            userInOrgaWithRole.setOrgaRole(newOrgaRole);
            userInOrgaWithRoleRepository.save(userInOrgaWithRole);
            return LocalizedStringVariables.SETPERSONADMININORGASUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.SETPERSONADMININORGAFAILUREMESSAGE;
        }
    }

    /**
     * Sets at an existing relation between user and organisation the role of the user to organizer.
     * @param organisationId ID of the organisation where the user will be organizer.
     * @param userMail Mail of the user who will be organizer.
     * @return String about success or failure.
     */
    public String setPersonOrganizer(long organisationId, String userMail) {
        User user = userService.getUserByMail(userMail);
        OrgaRole newOrgaRole = orgaRoleService.findByRole(EnumOrgaRole.ORGANIZER);
        UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), organisationId);

        try {
            userInOrgaWithRole.setOrgaRole(newOrgaRole);
            userInOrgaWithRoleRepository.save(userInOrgaWithRole);
            return LocalizedStringVariables.SETPERSONORGANIZERINORGASUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.SETPERSONORGANIZERINORGAFAILUREMESSAGE;
        }
    }

    /**
     * Sets at an existing relation between user and organisation the role of the user to normal user.
     * @param organisationId ID of the organisation where the user will be a normal user.
     * @param userMail Mail of the user who will be a normal user.
     * @return String about success or failure.
     */
    public String setPersonUser(long organisationId, String userMail) {
        User user = userService.getUserByMail(userMail);
        OrgaRole newOrgaRole = orgaRoleService.findByRole(EnumOrgaRole.USER);
        UserInOrgaWithRole userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser_IdAndOrganisation_Id(user.getId(), organisationId);

        try {
            userInOrgaWithRole.setOrgaRole(newOrgaRole);
            userInOrgaWithRoleRepository.save(userInOrgaWithRole);
            return LocalizedStringVariables.SETPERSONUSERINORGASUCCESSMESSAGE;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalizedStringVariables.SETPERSONUSERINORGAFAILUREMESSAGE;
        }
    }

    /**
     * Invited a user to an organisation by setting the Role INVITED and sending the user an email.
     * @param organisationId ID of the organisation.
     * @param userMail Mail of the user who will be invited.
     * @return String about success or failure.
     */
    public String inviteUserToOrganisation(long organisationId, String userMail) {
        Organisation organisation = organisationService.getOrganisationById(organisationId);
        User user = userService.getUserByMail(userMail);
        OrgaRole inviteRole = orgaRoleService.findByRole(EnumOrgaRole.INVITED);

        if (userInOrgaWithRoleRepository.existsByUser_IdAndOrganisation_Id(user.getId(), organisationId)) {
            return LocalizedStringVariables.INVITEDUSERALREADYPARTOFORGANISATIONMESSAGE;
        } else {
            //TODO Einladungsmail an user senden.

            try {
                UserInOrgaWithRole userInOrgaWithRole = new UserInOrgaWithRole();
                userInOrgaWithRole.setOrganisation(organisation);
                userInOrgaWithRole.setUser(user);
                userInOrgaWithRole.setOrgaRole(inviteRole);
                userInOrgaWithRoleRepository.save(userInOrgaWithRole);
                return LocalizedStringVariables.INVITEUSERTOORGANISATIONSUCCESSMESSAGE;
            } catch (Exception e) {
                e.printStackTrace();
                return LocalizedStringVariables.INVITEUSERTOORGANISATIONFAILUREMESSAGE;
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
}
