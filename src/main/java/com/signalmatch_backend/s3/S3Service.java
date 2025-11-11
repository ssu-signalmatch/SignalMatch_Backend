package com.signalmatch_backend.s3;

import ch.qos.logback.classic.Logger;
import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.s3.dto.PresignRequest;
import com.signalmatch_backend.s3.dto.PresignResponse;
import com.signalmatch_backend.s3.dto.UploadType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner presigner;
    private final S3Client s3Client;
    private final S3KeyResolver keyResolver;

    @Value("${aws.s3.credentials.bucket}")
    private String bucket;

    private static final Set<String> IMG_EXT = Set.of("jpg","jpeg","png");
    private static final Set<String> IR_EXT  = Set.of("pdf");
    private static final long MAX_IMG_BYTES = 5L * 1024 * 1024; //5MB
    private static final long MAX_IR_BYTES  = 50L * 1024 * 1024; //50MB

    public PresignResponse presignFor(UploadType type, Long ownerId, PresignRequest req) {
        // 1) 검증
        String ext = extractExt(req.fileName());
        String mime = Optional.ofNullable(req.mimeType()).orElse("");
        long len = Optional.ofNullable(req.contentLength()).orElse(0L);

        switch (type) {
            case PROFILE_IMAGE -> {
                if (!IMG_EXT.contains(ext)) {
                    throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
                }
                if (!mime.startsWith("image/")) {
                    throw new CustomException(ErrorCode.INVALID_MIME_TYPE);
                }
                if (len <= 0 || len > MAX_IMG_BYTES) {
                    throw new CustomException(ErrorCode.IMAGE_SIZE_EXCEEDED);
                }
            }
            case IR_FILE -> {
                if (!IR_EXT.contains(ext)) {
                    throw new CustomException(ErrorCode.INVALID_FILE_TYPE);
                }
                if (!"application/pdf".equals(mime)) {
                    throw new CustomException(ErrorCode.PDF_ONLY);
                }
                if (len <= 0 || len > MAX_IR_BYTES) {
                    throw new CustomException(ErrorCode.IR_SIZE_EXCEEDED);
                }
            }
        }

        // 2) 키 생성
        String key = switch (type) {
            case PROFILE_IMAGE -> keyResolver.profileImageKey(ownerId, ext);
            case IR_FILE -> keyResolver.irFileKey(ownerId, ext);
        };

        // 3) PutObjectRequest
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(mime)
                .build();

        // 4) presign
        PutObjectPresignRequest presign = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5)) //유효기간 5분
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest pre = presigner.presignPutObject(presign);

        // 5) 응답 (클라이언트가 그대로 헤더를 넣어 PUT하도록)
        Map<String,String> headers = new HashMap<>();
        pre.signedHeaders().forEach((k,v) -> headers.put(k, String.join(",", v)));

        return new PresignResponse(pre.url().toString(), "PUT", headers, key);
    }

    private String extractExt(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i < 0) throw new CustomException(ErrorCode.FILE_EXTENSION_REQUIRED);
        return fileName.substring(i + 1).toLowerCase();
    }

    public void deleteObject(String key) {
        DeleteObjectRequest req = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.deleteObject(req);
    }
}