package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.UserInOrgaWithRoleRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.stereotype.Service;

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

    public UserInOrgaWithRoleService(
            UserInOrgaWithRoleRepository userInOrgaWithRoleRepository,
            UserService userService, OrganisationService organisationService, OrgaRoleService orgaRoleService) {
        this.userInOrgaWithRoleRepository = userInOrgaWithRoleRepository;
        this.userService = userService;
        this.organisationService = organisationService;
        this.orgaRoleService = orgaRoleService;
    }


    /**
     * Retrieve information about orgas user is a part of
     * @param userId Id of the corresponding user
     * @return List of organisation objects
     */
    public List<Organisation> getOrgaForUser(long userId) {
        try {
            User user = userService.getUserById(userId);
            List <UserInOrgaWithRole> userInOrgaWithRoles = userInOrgaWithRoleRepository.findByUser(user);

            List <Organisation> organisationsForUser = null;

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

    public String leaveOrganisation(long organisationId, String userMail, String reason){
        try {
            User user = userService.getUserByMail(userMail);
            Organisation organisation = organisationService.getOrganisationById(organisationId);

            //Todo reason per mail an den admin o.ä. der Organisation

            List<UserInOrgaWithRole> userInOrgaWithRole = userInOrgaWithRoleRepository.findByUser(user);

            for (UserInOrgaWithRole deleteUserInOrgaWithRole: userInOrgaWithRole) {
                if(deleteUserInOrgaWithRole.getOrganisation() == organisation){
                    userInOrgaWithRoleRepository.delete(deleteUserInOrgaWithRole);
                    return LocalizedStringVariables.LEAVEORGANISATIONSUCCESSMESSAGE;
                }
            }

            return LocalizedStringVariables.USERISNOTINORGANISATIONMESSAGE;
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
}
