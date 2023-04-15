package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Preset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresetRepository extends JpaRepository<Preset, Long> {
}
