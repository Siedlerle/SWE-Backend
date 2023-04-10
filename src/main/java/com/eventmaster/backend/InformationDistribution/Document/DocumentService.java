package com.eventmaster.backend.InformationDistribution.Document;

import com.eventmaster.backend.EventManagement.Event;
import com.eventmaster.backend.EventManagement.EventRepository;
import com.eventmaster.backend.FileService.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final EventRepository eventRepository;

    public DocumentService(DocumentRepository documentRepository, EventRepository eventRepository){
        this.documentRepository = documentRepository;
        this.eventRepository = eventRepository;
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
