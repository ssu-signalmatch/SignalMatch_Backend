package com.signalmatch_backend.investor.controller;

import com.signalmatch_backend.common.domain.ApiResponse;
import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.investor.dto.InvestorProfileCreateRequest;
import com.signalmatch_backend.investor.dto.InvestorProfileCreateResponse;
import com.signalmatch_backend.investor.dto.InvestorProfileInfo;
import com.signalmatch_backend.investor.dto.InvestorProfileUpdateRequest;
import com.signalmatch_backend.investor.service.InvestorService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Investor",description = "투자자 관련 API입니다.")
public class InvestorController {
    private final InvestorService investorService;

    @PostMapping("/investors/me")
    @Operation(summary = "투자자 프로필 생성", description = "로그인한 사용자의 투자자 프로필을 생성합니다.")
    public ResponseEntity<ApiResponse<InvestorProfileCreateResponse>> investorProfileCreate(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody InvestorProfileCreateRequest request){
        Long userId = customUserDetails.getUser().getUserId();
        InvestorProfileCreateResponse response = investorService.createInvestorProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("투자자 프로필 생성이 완료되었습니다.",response));
    }

    @PatchMapping("/investors/me")
    @Operation(summary = "투자자 프로필 수정", description = "투자자 프로필을 수정합니다.")
    public ResponseEntity<ApiResponse<Void>> investorProfileUpdate(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody InvestorProfileUpdateRequest request){
        Long userId = customUserDetails.getUser().getUserId();
        investorService.updateInvestorProfile(userId,request);
        return ResponseEntity.ok(ApiResponse.success("투자자 프로필 수정이 완료되었습니다."));
    }

    @GetMapping("/investors/{userId}")
    @Operation(summary = "투자자 프로필 상세조회", description = "투자자 프로필을 상세조회합니다.")
    public ResponseEntity<ApiResponse<InvestorProfileInfo>> findInvestorProfile(@PathVariable Long userId){
        InvestorProfileInfo investorProfileInfo=investorService.findInvestorProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("투자자 프로필 상세조회가 완료되었습니다.",investorProfileInfo));
    }
}
