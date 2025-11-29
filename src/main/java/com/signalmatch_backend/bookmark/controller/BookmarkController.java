package com.signalmatch_backend.bookmark.controller;

import com.signalmatch_backend.bookmark.dto.BookmarkListResponse;
import com.signalmatch_backend.bookmark.dto.BookmarkRequest;
import com.signalmatch_backend.bookmark.dto.BookmarkResponse;
import com.signalmatch_backend.bookmark.dto.StartupBookmarkInfo;
import com.signalmatch_backend.bookmark.service.BookmarkService;
import com.signalmatch_backend.common.domain.ApiResponse;
import com.signalmatch_backend.user.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @DeleteMapping("/bookmarks/{targetUserId}")
    @Operation(summary = "즐겨찾기 삭제" ,description = "로그인한 사용자가 투자자 또는 스타트업을 즐겨찾기에서 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> bookmarkDelete(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long targetUserId){
        Long userId = customUserDetails.getUser().getUserId();
        bookmarkService.deleteBookmark(userId, targetUserId);
        return ResponseEntity.ok(ApiResponse.success("즐겨찾기가 삭제되었습니다."));
    }

    @GetMapping("/bookmarks")
    @Operation(summary = "즐겨찾기 목록 조회", description = "로그인한 사용자의 즐겨찾기 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<BookmarkListResponse>>> bookmarkGet(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long userId = customUserDetails.getUser().getUserId();
        List<BookmarkListResponse> response = bookmarkService.getBookmarkList(userId);
        return ResponseEntity.ok(ApiResponse.success("즐겨찾기 목록의 조회가 완료되었습니다.",response));
    }

    @GetMapping("/search/best")
    @Operation(summary = "스타트업 검색 인기 순위 조회", description = "스타트업 검색 인기 순위 Top 10을 조회합니다.")
    public ResponseEntity<ApiResponse<List<StartupBookmarkInfo>>> getTop10Rankings() {
        List<StartupBookmarkInfo> response = bookmarkService.getTop10Startups();
        return ResponseEntity.ok(ApiResponse.success("스타트업 검색 인기 순위 조회가 완료되었습니다.",response));
    }

}
