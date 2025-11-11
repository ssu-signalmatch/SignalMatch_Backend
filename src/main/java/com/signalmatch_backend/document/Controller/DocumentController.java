package com.signalmatch_backend.document.Controller;

import com.signalmatch_backend.common.domain.ApiResponse;
import com.signalmatch_backend.document.Service.DocumentService;
import com.signalmatch_backend.document.dto.DocumentCreateRequest;
import com.signalmatch_backend.document.dto.DocumentResponse;
import com.signalmatch_backend.user.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/documents")
@Tag(name = "Document", description = "문서 관련 API입니다.")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    @Operation(summary = "문서 등록하기", description = "presign url을 통해 등록한 문서를 DB에 저장하는 API입니다.")
    public ResponseEntity<ApiResponse<Void>> createDocument(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid DocumentCreateRequest documentRequest) {
        Long userId = customUserDetails.getUser().getUserId();
        documentService.createDocument(userId, documentRequest);
        return ResponseEntity.ok(ApiResponse.success("문서의 object key가 저장되었습니다."));
    }

    @GetMapping
    @Operation(summary = "문서 조회하기", description = "presign url을 통해 등록한 문서를 조회하는 API입니다.")
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getMyIrDocument(
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = customUserDetails.getUser().getUserId();
        List<DocumentResponse> response = documentService.getMyIrDocuments(userId);

        return ResponseEntity.ok(ApiResponse.success("문서가 조회되었습니다.",response));
    }
}
