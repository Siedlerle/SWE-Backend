package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.MessageResponse;
import com.eventmaster.backend.entities.Organisation;
import com.eventmaster.backend.entities.Preset;
import com.eventmaster.backend.repositories.PresetRepository;
import local.variables.LocalizedStringVariables;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private final DocumentService documentService;


    public PresetService(PresetRepository presetRepository, OrganisationService organisationService, DocumentService documentService) {
        this.presetRepository = presetRepository;
        this.organisationService = organisationService;
        this.documentService = documentService;
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
    public MessageResponse createPreset(long organisationId, Preset preset, MultipartFile image) {
        try {
            Organisation organisation = this.organisationService.getOrganisationById(organisationId);
            preset.setOrganisation(organisation);
            this.presetRepository.save(preset);
            if (image != null) {
                String imageUrl = documentService.savePresetImage(preset.getId(), image);
                preset.setImage(imageUrl);
                this.presetRepository.save(preset);
            }

            return  MessageResponse.builder()
                    .message(LocalizedStringVariables.PRESETCREATEDSUCCESSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return  MessageResponse.builder()
                    .message(LocalizedStringVariables.PRESETCREATEDFAILUREMESSAGE)
                    .build();
        }
    }

    /**
     * An existing preset is changed in the database.
     * @param preset New preset with the new data which will overwrite the old data.
     * @return String about success of failure.
     */
    public MessageResponse changePreset(Preset preset, MultipartFile image) {
        try {
            Preset updatedPreset = this.presetRepository.findById(preset.getId());
            updatedPreset.setId(preset.getId());
            updatedPreset.setOrganisation(preset.getOrganisation());
            updatedPreset.setName(preset.getName());
            updatedPreset.setType(preset.getType());
            updatedPreset.setDescription(preset.getDescription());
            updatedPreset.setLocation(preset.getLocation());
            updatedPreset.setStartDate(preset.getStartDate());
            updatedPreset.setEndDate(preset.getEndDate());
            updatedPreset.setStartTime(preset.getStartTime());
            updatedPreset.setEndTime(preset.getEndTime());
            if (image != null) {
                String oldImageLink = preset.getImage();
                File oldfile = new File("src/main/upload" + oldImageLink);
                if (oldfile.exists()) {
                    oldfile.delete();
                }
                String imageUrl = documentService.savePresetImage(preset.getId(), image);
                updatedPreset.setImage(imageUrl);
            }

            this.presetRepository.save(preset);
            return  MessageResponse.builder()
                    .message(LocalizedStringVariables.PRESETCHANGEDSUCCESSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return  MessageResponse.builder()
                    .message(LocalizedStringVariables.PRESETCHANGEDFAILUREMESSAGE)
                    .build();
        }
    }

    /**
     * A preset corresponding to the presetId is being deleted from the database.
     * @param presetId ID of the preset which will be deleted.
     * @return String about success of failure.
     */
    public MessageResponse deletePreset(long presetId) {
        try {
            this.presetRepository.deleteById(presetId);
            return  MessageResponse.builder()
                    .message(LocalizedStringVariables.PRESETDELETEDSUCCESSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return  MessageResponse.builder()
                    .message(LocalizedStringVariables.PRESETDELETEDFAILUREMESSAGE)
                    .build();
        }
    }
}
