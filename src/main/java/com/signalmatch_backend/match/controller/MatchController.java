package com.signalmatch_backend.match.controller;


import com.signalmatch_backend.common.domain.ApiResponse;
import com.signalmatch_backend.match.dto.MatchCreateRequest;
import com.signalmatch_backend.match.dto.MatchRequestedEvent;
import com.signalmatch_backend.match.service.MatchService;
import com.signalmatch_backend.user.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
@Tag(name = "Matching", description = "매칭 관련 API입니다.")
public class MatchController {

    public final MatchService matchService;

    @PostMapping
    @Operation(summary = "매칭 요청하기", description = "투자자와 스타트업 간의 매칭을 생성하는 API 입니다.")
    public ResponseEntity<ApiResponse<Long>> createMatch(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody MatchCreateRequest matchCreateRequest) {
        Long matchId = matchService.createMatch(matchCreateRequest.investorId(), matchCreateRequest.startupId());
        return ResponseEntity.ok(ApiResponse.success("매칭이 생성되었습니다.", matchId));
    }
}
