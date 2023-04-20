package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.UserInOrgaWithRoleRepository;
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

    public UserInOrgaWithRoleService(
            UserInOrgaWithRoleRepository userInOrgaWithRoleRepository,
            UserService userService, OrganisationService organisationService) {
        this.userInOrgaWithRoleRepository = userInOrgaWithRoleRepository;
        this.userService = userService;
        this.organisationService = organisationService;
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
        try {
            User user = userService.getUserByMail(userMail);
            Organisation organisation = organisationService.getOrganisationById(organisationId);

            UserInOrgaWithRole userInOrgaWithRole = new UserInOrgaWithRole();
            userInOrgaWithRole.setUser(user);
            userInOrgaWithRole.setOrganisation(organisation);

            //Todo Rolle für Anfrage festlegen

            userInOrgaWithRoleRepository.save(userInOrgaWithRole);
            return "Requested join successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Request failed";
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
                    return "Removed successfully";
                }
            }

            return "User has no matching organisation";
        } catch (Exception e) {
            e.printStackTrace();
            return "Leaving organisation failed";
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
                    return "Removed successfully";
                }
            }

            return "User is not in organisation";
        } catch (Exception e) {
            e.printStackTrace();
            return "Remove failed";
        }
    }
}
