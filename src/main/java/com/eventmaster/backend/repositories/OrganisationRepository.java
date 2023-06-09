package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the Organisations in the database.
 *
 * @author Fabian Eilber
 */
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
    Organisation findById(long organisationId);
}
