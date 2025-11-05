package com.signalmatch_backend.bookmark.controller;

import com.signalmatch_backend.bookmark.dto.BookmarkRequest;
import com.signalmatch_backend.bookmark.dto.BookmarkResponse;
import com.signalmatch_backend.bookmark.service.BookmarkService;
import com.signalmatch_backend.common.domain.ApiResponse;
import com.signalmatch_backend.user.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Bookmark",description = "즐겨찾기 관련 API입니다.")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/bookmarks")
    @Operation(summary = "즐겨찾기 추가" ,description = "로그인한 사용자의 관심있는 투자자 또는 스타트업을 즐겨찾기에 추가합니다.")
    public ResponseEntity<ApiResponse<BookmarkResponse>> bookmarkAdd(@AuthenticationPrincipal CustomUserDetails customUserDetails,@RequestBody BookmarkRequest request){
        Long userId = customUserDetails.getUser().getUserId();
        BookmarkResponse response = bookmarkService.addBookmark(userId, request);
        return ResponseEntity.ok(ApiResponse.success("즐겨찾기가 추가되었습니다.",response));
    }

}
