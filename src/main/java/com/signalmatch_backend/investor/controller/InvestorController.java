package com.signalmatch_backend.investor.controller;

import com.signalmatch_backend.common.domain.ApiResponse;
import com.signalmatch_backend.investor.dto.InvestorProfileCreateRequest;
import com.signalmatch_backend.investor.dto.InvestorProfileCreateResponse;
import com.signalmatch_backend.investor.service.InvestorService;
import com.signalmatch_backend.user.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Investor",description = "투자자 관련 API입니다.")
public class InvestorController {
    private final InvestorService investorService;

    @PostMapping("/investors/me")
    @Operation(summary = "투자자 프로필 생성", description = "로그인한 사용자의 투자자 프로필을 생성합니다.")
    public ResponseEntity<ApiResponse> investorProfileCreate(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody InvestorProfileCreateRequest request){
        Long userId = customUserDetails.getUser().getUserId();
        InvestorProfileCreateResponse response = investorService.createInvestorProfile(
            userId, request);
        return ResponseEntity.ok(ApiResponse.success("투자자 프로필 생성이 완료되었습니다.",response));
    }

}
