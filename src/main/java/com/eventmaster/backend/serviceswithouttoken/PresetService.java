package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.entities.Organisation;
import com.eventmaster.backend.entities.Preset;
import com.eventmaster.backend.repositories.PresetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of presets
 *
 * @author Fabian Unger
 */
@Service
public class PresetService {
    private final PresetRepository presetRepository;
    private final OrganisationService organisationService;


    public PresetService(PresetRepository presetRepository, OrganisationService organisationService) {
        this.presetRepository = presetRepository;
        this.organisationService = organisationService;
    }

    /**
     * Searches the presets from a given organisation.
     * @param organisationId ID of the organisation which contains the presets.
     * @return List of presets
     */
    public List<Preset> getPresetsForOrganisation(long organisationId) {
        try {
            Organisation organisation = this.organisationService.getOrganisationById(organisationId);
            List<Preset> presetsFromOrganisation = this.presetRepository.findByOrganisation(organisation);
            return presetsFromOrganisation;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A preset is added to the database and connected to his organisation.
     * @param organisationId ID of the organisation which will contain the preset.
     * @param preset Preset which will be added.
     * @return String about success of failure.
     */
    public String createPreset(long organisationId, Preset preset) {
        try {
            Organisation organisation = this.organisationService.getOrganisationById(organisationId);
            preset.setOrganisation(organisation);
            this.presetRepository.save(preset);
            return "Preset created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Creation failed";
        }
    }

    /**
     * An existing preset is changed in the database.
     * @param preset New preset with the new data which will overwrite the old data.
     * @return String about success of failure.
     */
    public String changePreset(Preset preset) {
        try {
            this.presetRepository.save(preset);
            return "Preset changed successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Change failed";
        }
    }

    /**
     * A preset corresponding to the presetId is being deleted from the database.
     * @param presetId ID of the preset which will be deleted.
     * @return String about success of failure.
     */
    public String deletePreset(long presetId) {
        try {
            this.presetRepository.deleteById(presetId);
            return "Preset successfully deleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "Deletion failed";
        }
    }
}
