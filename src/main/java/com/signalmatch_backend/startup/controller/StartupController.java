package com.signalmatch_backend.startup.controller;

import com.signalmatch_backend.common.domain.ApiResponse;
import com.signalmatch_backend.investor.dto.InvestorProfileUpdateResponse;
import com.signalmatch_backend.startup.dto.StartupProfileCreateRequest;
import com.signalmatch_backend.startup.dto.StartupProfileCreateResponse;
import com.signalmatch_backend.startup.dto.StartupProfileInfo;
import com.signalmatch_backend.startup.dto.StartupProfileUpdateRequest;
import com.signalmatch_backend.startup.dto.StartupProfileUpdateResponse;
import com.signalmatch_backend.startup.service.StartupService;
import com.signalmatch_backend.user.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Startup",description = "스타트업 관련 API 입니다.")
public class StartupController {
    private final StartupService startupService;
    @PostMapping("/startups/me")
    @Operation(summary = "스타트업 프로필 생성",description = "사용자의 스타트업 프로필을 생성합니다.")
    public ResponseEntity<ApiResponse<StartupProfileCreateResponse>> startupProfileCreate(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody StartupProfileCreateRequest request){
        Long userId = customUserDetails.getUser().getUserId();
        StartupProfileCreateResponse response = startupService.createStartupProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("스타트업 프로필 생성이 완료되었습니다.",response));
    }

    @PatchMapping("/startups/me")
    @Operation(summary = "스타트업 프로필 수정", description = "스타트업 프로필을 수정합니다.")
    public ResponseEntity<ApiResponse<StartupProfileUpdateResponse>> startupProfileUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody StartupProfileUpdateRequest request){
        Long userId = customUserDetails.getUser().getUserId();
        StartupProfileUpdateResponse response = startupService.updateStartupProfile(userId,request);
        return ResponseEntity.ok(ApiResponse.success("스타트업 프로필 수정이 완료되었습니다.",response));
    }

    @GetMapping("/startups/{userId}")
    @Operation(summary = "스타트업 프로필 상세조회", description = "스타트업 프로필을 상세 조회 합니다.")
    public ResponseEntity<ApiResponse<StartupProfileInfo>> findStartupProfile(@PathVariable Long userId){
        StartupProfileInfo startupProfileInfo = startupService.findStartupProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("스타트업 프로필 상세조회가 완료되었습니다.",startupProfileInfo));
    }
}
