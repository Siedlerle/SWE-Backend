package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Preset;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface is used to access the Presets in the database.
 *
 * @author Fabian Unger
 */
public interface PresetRepository extends JpaRepository<Preset, Long> {
}
