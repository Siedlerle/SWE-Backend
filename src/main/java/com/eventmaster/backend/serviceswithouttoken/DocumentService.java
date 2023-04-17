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
import java.util.List;

/**
 * A class which receives and processes the requests of multiple controllers concerning the management of documents
 *
 * @author Fabian Eilber
 */

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    //Todo Zugriff über Service nicht direkt auf eventRepository
    private final EventRepository eventRepository;

    public DocumentService(DocumentRepository documentRepository, EventRepository eventRepository){
        this.documentRepository = documentRepository;
        this.eventRepository = eventRepository;
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
     * Searching for a file by its filename and filepath
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



    public void createDocument(long eventId, MultipartFile multipartFile) throws IOException {
        Event event = eventRepository.findById(eventId).get();

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();

        Document doc = new Document();
        doc.setName(fileName);
        doc.setSize(size);
        doc.setEvent(event);
        doc = documentRepository.save(doc);

        String filecode = FileUploadUtil.saveFile(eventId, fileName, multipartFile,doc.getId());

        doc.setDownloadUri("event/" + eventId + "/" + filecode);
    }

    public void deleteDocument(Document doc) {
        Document document = documentRepository.findById(doc.getId()).get();

        long docId = document.getId();
        String uri = document.getDownloadUri();
        long eventId = document.getEvent().getId();

        Event event = document.getEvent();
        event.removeDocument(document);

        document.setEvent(null);

        documentRepository.delete(document);

        File file = new File("/media/eventfiles/" + uri);
        file.delete();
    }
}
