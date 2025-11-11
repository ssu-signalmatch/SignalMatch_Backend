package com.signalmatch_backend.document.repository;

import com.signalmatch_backend.document.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    // IR 전용(= companies/ 로 시작)
    List<Document> findByUserIdAndObjectKeyStartingWithOrderByCreatedAtDesc(
            Long userId, String prefix);

    // 프로필 이미지 최신 1건
    Optional<Document> findFirstByUserIdAndObjectKeyStartingWithOrderByCreatedAtDesc(
            Long userId, String prefix);

    void deleteByDocumentIdAndUserId(Long documentId, Long userId);

    Optional<Document> findByDocumentIdAndUserId(Long documentId, Long userId);
}


