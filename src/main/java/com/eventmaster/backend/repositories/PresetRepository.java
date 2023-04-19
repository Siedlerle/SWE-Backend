package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Organisation;
import com.eventmaster.backend.entities.Preset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface is used to access the Presets in the database.
 *
 * @author Fabian Unger
 */
public interface PresetRepository extends JpaRepository<Preset, Long> {
    List<Preset> findByOrganisation(Organisation organisation);
    Preset findById(long presetId);
}
