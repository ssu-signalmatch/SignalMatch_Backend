package com.signalmatch_backend.document.Service;

import com.signalmatch_backend.document.domain.Document;
import com.signalmatch_backend.document.dto.DocumentCreateRequest;
import com.signalmatch_backend.document.repository.DocumentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Transactional
    public void createDocument(Long userId, DocumentCreateRequest request) {
        Document document = Document.builder()
                .userId(userId)
                .objectKey(request.ObjectKey())
                .build();

        documentRepository.save(document);
    }
}
