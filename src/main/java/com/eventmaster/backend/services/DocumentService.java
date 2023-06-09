package com.eventmaster.backend.services;

import com.eventmaster.backend.entities.*;
import com.eventmaster.backend.repositories.DocumentRepository;
import local.variables.LocalizedStringVariables;
import org.apache.commons.io.FileUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of documents
 *
 * @author Fabian Eilber
 * @author Fabian Unger
 */

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    private final EventService eventService;

    public DocumentService(DocumentRepository documentRepository,
                           @Lazy EventService eventService) {
        this.documentRepository = documentRepository;
        this.eventService = eventService;
    }

    /**
     * Searches for a document by a given ID.
     *
     * @param docId Id of the document.
     * @return Document with the correct ID.
     */
    public Document getDocumentById(long docId) {
        try {
            return documentRepository.findById(docId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Searching for all files corresponding to an event
     *
     * @param eventId Id of the event the file is searched for
     * @return List of document objects as they store the data
     */
    public List<Document> getFilesForEvent(long eventId) {
        return documentRepository.findDocumentByEventId(eventId);
    }

    /**
     * Searching for a file by its filename and filepath and downloads it.
     *
     * @param uri      filepath of the file
     * @param filename name of the file
     * @return Resource which transfers the data of a file
     * @throws IOException
     */
    public ResponseEntity<Resource> downloadFile(String uri, String filename) throws IOException {
        //String filePath = "/media/eventfiles/" + uri;
        String filePath = "src/main/upload/event_documents/" + uri;
        org.springframework.core.io.Resource resource = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    /**
     * Creates a document by passing the file to the saveFile method. It receives the download link from this and saves it in the document object.
     *
     * @param eventId       ID of the event which contains the document.
     * @param multipartFile File which will be saved.
     * @return String about success or failure.
     * @throws IOException
     */
    public MessageResponse createDocument(long eventId, MultipartFile multipartFile) throws IOException {
        Event event = eventService.getEventById(eventId);

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();

        Document doc = new Document();
        doc.setName(fileName);
        doc.setSize(size);
        doc.setEvent(event);
        try {
            //doc =
            String filecode = saveFile(eventId, doc.getId(), fileName, multipartFile);
            //doc.setDownloadUri("src/main/upload/event_documents/event/"+eventId+"/"+filecode);
            doc.setDownloadUri("event/" + eventId + "/" + filecode);
            documentRepository.save(doc);
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.DOCUMENTCREATEDSUCCESSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.DOCUMENTCREATEDFAILUREMESSAGE)
                    .build();
        }
    }

    /**
     * Deletes a document belonging to an event from the database and the server.
     *
     * @param docId ID of the document which will be deleted.
     * @return String about success or failure.
     */
    public MessageResponse deleteDocument(long docId) {
        try {
            Document document = getDocumentById(docId);

            String uri = document.getDownloadUri();

            Event event = document.getEvent();
            event.removeDocument(document);

            document.setEvent(null);
            documentRepository.delete(document);


            File file = new File("src/main/upload/event_documents/" + uri);
            file.delete();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.DOCUMENTDELETEDSUCCESSMESSAGE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResponse.builder()
                    .message(LocalizedStringVariables.DOCUMENTDELETEDFAILUREMESSAGE)
                    .build();
        }
    }

    /**
     * Saves a file on the server at e specific path.
     *
     * @param eventId       ID of the event which contains the file.
     * @param documentId    ID of the document.
     * @param fileName      Name of the file which will be saved.
     * @param multipartFile File which will be saved.
     * @return ID of the document.
     * @throws IOException
     */
    public String saveFile(long eventId, long documentId, String fileName, MultipartFile multipartFile) throws IOException {
        //Path uploadPath = Paths.get("/media/eventfiles/event/" + eventId);
        Path uploadPath = Paths.get("src/main/upload/event_documents/event/" + eventId);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(String.valueOf(documentId)); //fileCode
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        return String.valueOf(documentId);
    }

    /**
     * Saves the image for an event in the resources and returns the filepath.
     *
     * @param eventId ID of the event which the image is belonging to.
     * @param image   Image of the event.
     * @return FilePath of the image.
     * @throws IOException
     */
    public String saveEventImage(long eventId, MultipartFile image) throws IOException {
        String fileExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        Path uploadPath = Paths.get("src/main/upload/event_images/");
        //"src/main/upload/"
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        if (Files.exists(Path.of(uploadPath + String.valueOf(eventId) + "." + fileExtension))) {
            File file = new File("src/main/upload/event_images/" + String.valueOf(eventId) + "." + fileExtension);
            file.delete();
        }

        try (InputStream inputStream = image.getInputStream()) {
            Path filePath = uploadPath.resolve(String.valueOf(eventId) + "." + fileExtension);
            String fileName = "/event_images/" + String.valueOf(eventId) + "." + fileExtension;
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + eventId, ioe);
        }
    }

    /**
     * Saves the image for an present in the resources and returns the filepath.
     *
     * @param presetId ID of the preset which the image is belonging to.
     * @param image    Image of the preset.
     * @throws IOException
     * @returnFilePath of the image.
     */
    public String savePresetImage(long presetId, MultipartFile image) throws IOException {
        String fileExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        Path uploadPath = Paths.get("src/main/upload/preset_images/");
        //"src/main/upload/"
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = image.getInputStream()) {
            Path filePath = uploadPath.resolve(String.valueOf(presetId) + "." + fileExtension);
            String fileName = "/preset_images/" + String.valueOf(presetId) + "." + fileExtension;
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + presetId, ioe);
        }
    }

    public String saveOrgaImage(long orgaId, MultipartFile image) throws IOException {
        String fileExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        Path uploadPath = Paths.get("src/main/upload/orga_images/");
        //"src/main/upload/"
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = image.getInputStream()) {
            Path filePath = uploadPath.resolve(String.valueOf(orgaId) + "." + fileExtension);
            String fileName = "/orga_images/" + String.valueOf(orgaId) + "." + fileExtension;
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + orgaId, ioe);
        }
    }

    public boolean deleteDocumentsForEvent(Event event) {
//        List<Document> documents = event.getDocuments().stream().toList();
//        //Path deletePath = Paths.get("src/main/upload/event_documents/event/" + event.getId() + "/");
//        for (Document d : documents) {
        Path deletePath = Paths.get("src/main/upload/event_documents/event/" + event.getId());
        try {
            FileUtils.deleteDirectory(deletePath.toFile());
            Files.deleteIfExists(deletePath);
        } catch (IOException e) {
            return false;
        }
//        }
        return true;
    }

    public boolean deletePresetsImagesForOrganisation(Organisation organisation) {
        List<Preset> presets = organisation.getPresets().stream().toList();
        //Path deletePath = Paths.get("src/main/upload");
        for (Preset p : presets) {
            if (p.getImage() == null) continue;
            Path deletePath = Paths.get("src/main/upload" + p.getImage());
            try {
                Files.deleteIfExists(deletePath);
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    public boolean deleteEventImageForEvent(Event event) {
        if (event.getImage() == null) return true;
        Path deletePath = Paths.get("src/main/upload" + event.getImage());
        try {
            Files.deleteIfExists(deletePath);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean deleteImageForOrganisation(Organisation organisation) {
        if (organisation.getImage() == null) return true;
        Path deletePath = Paths.get("src/main/upload" + organisation.getImage());
        try {
            Files.deleteIfExists(deletePath);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

