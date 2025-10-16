package com.signalmatch_backend.match.controller;


import com.signalmatch_backend.common.domain.ApiResponse;
import com.signalmatch_backend.match.dto.*;
import com.signalmatch_backend.match.service.MatchService;
import com.signalmatch_backend.user.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Long userId = customUserDetails.getUser().getUserId();
        Long matchId = matchService.createMatch(userId, matchCreateRequest.investorId(), matchCreateRequest.startupId());
        return ResponseEntity.ok(ApiResponse.success("매칭이 생성되었습니다.", matchId));
    }

    @PostMapping("/{matchId}/accept")
    @Operation(summary = "매칭 수락하기", description = "요청된 매칭을 수락하는 API 입니다.")
    public ResponseEntity<ApiResponse<Void>> acceptMatch(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long matchId
    ){
        Long userId = customUserDetails.getUser().getUserId();
        matchService.acceptMatch(userId, matchId);
        return ResponseEntity.ok(ApiResponse.success("매칭이 수락되었습니다."));

    }

    @PostMapping("/{matchId}/reject")
    @Operation(summary = "매칭 거부하기", description = "요청된 매칭을 거부하는 API 입니다.")
    public ResponseEntity<ApiResponse<Void>> rejectMatch(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long matchId,
            @RequestBody(required = false) RejectRequest body
    ){
        Long userId = customUserDetails.getUser().getUserId();
        matchService.rejectMatch(
                userId,
                matchId,
                body == null ? null : body.reasonCode(),
                body == null ? null : body.reasonText()
        );
        return ResponseEntity.ok(ApiResponse.success("매칭이 거부되었습니다."));
    }

    @PostMapping("/{matchId}/cancel")
    @Operation(summary = "매칭 해제하기", description = "매칭을 해제하는 API 입니다.")
    public ResponseEntity<ApiResponse<Void>> cancelMatch(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long matchId,
            @RequestBody(required = false)CancelRequest body
            ){
        Long userId = customUserDetails.getUser().getUserId();
        matchService.cancelMatch(
                userId,
                matchId,
                body == null ? null : body.reasonCode(),
                body == null ? null : body.reasonText()
        );
        return ResponseEntity.ok(ApiResponse.success("매칭이 해제되었습니다."));
    }


    @GetMapping
    @Operation(summary = "매칭 목록 조회", description = "해당 유저의 매칭 목록을 조회하는 API 입니다.")
    public ResponseEntity<ApiResponse<List<MatchResponse>>> getMyMatches(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        Long userId = customUserDetails.getUser().getUserId();
        List<MatchResponse> response = matchService.getMyMatches(userId);
        return ResponseEntity.ok(ApiResponse.success("매칭 목록이 조회되었습니다.",response));
    }

}
