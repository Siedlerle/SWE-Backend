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


    public List<Organisation> getOrgaForUser(long userId) {
        try {
            User user = userService.getUserById(userId);
            List <UserInOrgaWithRole> userInOrgaWithRoles = userInOrgaWithRoleRepository.findByUser(user);

            List <Organisation> organisationsForUser = null;

            for(UserInOrgaWithRole orgasForUser: userInOrgaWithRoles){
                organisationsForUser.add(orgasForUser.getOrganization());
            }

            return organisationsForUser;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
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
                if (userInOrgaWithRole.getOrganization().getId() == organisation.getId())
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
