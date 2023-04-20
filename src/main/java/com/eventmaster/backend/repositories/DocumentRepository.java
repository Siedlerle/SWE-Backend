package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface is used to access the documents in the database.
 *
 * @author Fabian Eilber
 */
public interface DocumentRepository extends JpaRepository<Document,Long> {
    Document findById(long docId);
    List<Document> findDocumentByEventId(long eventId);

}

