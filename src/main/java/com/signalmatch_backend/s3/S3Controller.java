package com.signalmatch_backend.s3;

import com.signalmatch_backend.common.domain.ApiResponse;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.s3.dto.PresignRequest;
import com.signalmatch_backend.s3.dto.PresignResponse;
import com.signalmatch_backend.s3.dto.UploadType;
import com.signalmatch_backend.user.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
@Tag(name = "S3", description = "S3 Presign URL 관련 API입니다.")
@PreAuthorize("isAuthenticated()")
public class S3Controller {

    private final S3Service s3UploadService;

    @PostMapping("/profile-image/presign")
    @Operation(summary = "프로필 업로드 url 생성",description = "프로필 사진을 s3에 업로드 하는 Presign url을 발급합니다.")
    public ResponseEntity<ApiResponse<PresignResponse>> presignProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                       @RequestBody PresignRequest req) {
        PresignResponse response = s3UploadService.presignFor(UploadType.PROFILE_IMAGE, customUserDetails.getUser().getUserId(), req);
        return ResponseEntity.ok(ApiResponse.success("프로필 업로드 url이 발급되었습니다",response));
    }

    @PostMapping("/ir/presign")
    @Operation(summary = "스타트업 IR 업로드 url 생성",description = "IR을 s3에 업로드 하는 Presign url을 발급합니다.")
    public ResponseEntity<ApiResponse<PresignResponse>> presignIr(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                  @RequestBody PresignRequest req,
                                                                  @RequestParam Long startupId) {
        PresignResponse response = s3UploadService.presignFor(UploadType.IR_FILE, startupId, req);
        return ResponseEntity.ok(ApiResponse.success("IR 업로드 url이 발급되었습니다",response));

    }


}