package com.eventmaster.backend.repositories;

import com.eventmaster.backend.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,Long> {
}

