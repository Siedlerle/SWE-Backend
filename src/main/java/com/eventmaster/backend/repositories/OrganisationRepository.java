package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
    Organisation findById(long organisationId);
}
