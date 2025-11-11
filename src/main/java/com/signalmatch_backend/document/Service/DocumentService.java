package com.signalmatch_backend.document.Service;

import com.signalmatch_backend.common.config.CloudFrontProperties;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.document.domain.Document;
import com.signalmatch_backend.document.dto.DocumentCreateRequest;
import com.signalmatch_backend.document.dto.DocumentResponse;
import com.signalmatch_backend.document.repository.DocumentRepository;
import com.signalmatch_backend.s3.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final CloudFrontProperties cloudFrontProperties;
    private final S3Service s3Service;
    private static final String IR_PREFIX = "companies/";            // IR만
    private static final String PROFILE_PREFIX_FMT = "users/%d/profile/"; // 프로필용

    @Transactional
    public void createDocument(Long userId, DocumentCreateRequest request) {
        Document document = Document.builder()
                .userId(userId)
                .objectKey(request.ObjectKey())
                .build();

        documentRepository.save(document);
    }

    /** 내 문서 조회: IR만 */
    public List<DocumentResponse> getMyIrDocuments(Long userId) {
        String domain = trim(cloudFrontProperties.getDomain());
        return documentRepository
                .findByUserIdAndObjectKeyStartingWithOrderByCreatedAtDesc(userId, IR_PREFIX)
                .stream()
                .map(d -> toResponse(d, domain))
                .toList();
    }


    /** 유저 프로필 이미지 최신 URL (없으면 null) */
    public String getLatestProfileImageUrl(Long userId) {
        String domain = trim(cloudFrontProperties.getDomain());
        String prefix = PROFILE_PREFIX_FMT.formatted(userId);
        return documentRepository
                .findFirstByUserIdAndObjectKeyStartingWithOrderByCreatedAtDesc(userId, prefix)
                .map(doc -> domain + "/" + doc.getObjectKey())
                .orElse(null);
    }

    private DocumentResponse toResponse(Document doc, String domain) {
        String key = doc.getObjectKey();
        String fileName = key.substring(key.lastIndexOf('/') + 1);
        return new DocumentResponse(doc.getDocumentId(), key, domain + "/" + key, fileName);
    }


    private String trim(String s) { return (s != null && s.endsWith("/")) ? s.substring(0, s.length()-1) : s; }

    @Transactional
    public void deleteMyDocument(Long userId, Long documentId) {
        Document doc = documentRepository.findByDocumentIdAndUserId(documentId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.DOCUMENT_NOT_FOUND));

        String key = doc.getObjectKey();

        s3Service.deleteObject(key);

        documentRepository.deleteByDocumentIdAndUserId(documentId, userId);
    }
}
