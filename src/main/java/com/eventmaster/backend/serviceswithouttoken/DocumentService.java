package com.eventmaster.backend.serviceswithouttoken;

import com.eventmaster.backend.FileService.FileUploadUtil;
import com.eventmaster.backend.entities.Document;
import com.eventmaster.backend.entities.Event;
import com.eventmaster.backend.repositories.DocumentRepository;
import com.eventmaster.backend.repositories.EventRepository;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public DocumentService(DocumentRepository documentRepository, EventService eventService){
        this.documentRepository = documentRepository;
        this.eventService = eventService;
    }

    /**
     * Searches for a document by a given ID.
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
     * @param eventId Id of the event the file is searched for
     * @return List of document objects as they store the data
     */
    public List<Document> getFilesForEvent(long eventId){
        return documentRepository.findDocumentByEventId(eventId);
    }

    /**
     * Searching for a file by its filename and filepath and downloads it.
     * @param uri filepath of the file
     * @param filename name of the file
     * @return Resource which transfers the data of a file
     * @throws IOException
     */
    public ResponseEntity<Resource> downloadFile(String uri, String filename) throws IOException {
        String filePath = "/media/eventfiles/" + uri;

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
     * @param eventId ID of the event which contains the document.
     * @param multipartFile File which will be saved.
     * @return String about success or failure.
     * @throws IOException
     */
    public String createDocument(long eventId, MultipartFile multipartFile) throws IOException {
        Event event = eventService.getEventById(eventId);

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();

        Document doc = new Document();
        doc.setName(fileName);
        doc.setSize(size);
        doc.setEvent(event);
        try{
            doc = documentRepository.save(doc);
            String filecode = saveFile(eventId,doc.getId(), fileName, multipartFile);
            doc.setDownloadUri("event/" + eventId + "/" + filecode);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
    }

    /**
     * Deletes a document belonging to an event from the database and the server.
     * @param docId ID of the document which will be deleted.
     * @return String about success or failure.
     */
    public String deleteDocument(long docId) {
        Document document = getDocumentById(docId);

        String uri = document.getDownloadUri();

        Event event = document.getEvent();

        try {
            event.removeDocument(document);
            document.setEvent(null);
            documentRepository.delete(document);

            File file = new File("/media/eventfiles/" + uri);
            file.delete();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
    }

    /**
     * Saves a file on the server at e specific path.
     * @param eventId ID of the event which contains the file.
     * @param documentId ID of the document.
     * @param fileName Name of the file which will be saved.
     * @param multipartFile File which will be saved.
     * @return ID of the document.
     * @throws IOException
     */
    public String saveFile(long eventId, long documentId, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get("/media/eventfiles/event/" + eventId);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(String.valueOf(documentId));//fileCode
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        return String.valueOf(documentId);
    }
}
