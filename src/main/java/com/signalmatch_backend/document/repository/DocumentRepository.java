package com.signalmatch_backend.document.repository;

import com.signalmatch_backend.document.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findAllByUserId(Long userId);
}
