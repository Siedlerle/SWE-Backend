package com.eventmaster.backend.InformationDistribution.Document;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/event")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Uploading Files to a corresponding event
     * @param multipartFile The file upload
     * @param eventId Id of the event the file is meant for
     */
    @PostMapping("/event/uploadFile/{eventId}")
    public void uploadFileToEvent(@RequestParam("file") MultipartFile multipartFile, @PathVariable long eventId) throws IOException {
        documentService.createDocument(eventId, multipartFile);
    }

    /**
     * Deletes a Document
     * @param doc Document which is going to be deleted
     */
    @PostMapping("/deleteDocument")
    public void deleteDocument(@RequestBody Document doc) {
        documentService.deleteDocument(doc);
    }

}
