package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.EnumOrgaRole;
import com.eventmaster.backend.entities.OrgaRole;
import com.eventmaster.backend.repositories.OrgaRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of orgaroles
 *
 * @author Fabian Unger
 */
@Service
public class OrgaRoleService {
    private final OrgaRoleRepository orgaRoleRepository;

    public OrgaRoleService(OrgaRoleRepository orgaRoleRepository) {
        this.orgaRoleRepository = orgaRoleRepository;
    }

    /**
     * Creates the roles for organizations on build and stores them in the database if they don't already exist.
     */
    @PostConstruct
    public void init(){
        EnumOrgaRole roles;

        for (EnumOrgaRole role : EnumOrgaRole.values()) {
            if (findByRole(role) == null) {
                try {
                    OrgaRole staticOrgaRole = new OrgaRole();
                    staticOrgaRole.setId((long) role.ordinal());
                    staticOrgaRole.setRole(role);
                    orgaRoleRepository.save(staticOrgaRole);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Searches for the OrgaRole for the associated role in the database and returns it.
     * @param role Name of role.
     * @return OrgaRole object if existing
     */
    public OrgaRole findByRole(EnumOrgaRole role) {
        try {
            return orgaRoleRepository.findByRole(role);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Finds the OrgaRole for the id in the database and returns it.
     * @param id Id of the OrgaRole
     * @return OrgaRole object if existing
     */
    public OrgaRole findById(long id) {
        try {
            return orgaRoleRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
