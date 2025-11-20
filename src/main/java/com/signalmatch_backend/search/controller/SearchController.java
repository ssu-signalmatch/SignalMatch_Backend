package com.signalmatch_backend.search.controller;

import com.signalmatch_backend.common.domain.ApiResponse;
import com.signalmatch_backend.search.dto.SearchResponse;
import com.signalmatch_backend.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
@Tag(name = "Search",description = "통합검색 API 입니다.")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    @Operation(summary = "통합 검색",description ="키워드와 산업분야로 스타트업과 투자자를 검색합니다.")
    public ResponseEntity<ApiResponse<SearchResponse>> search(
        String keyword,
        @RequestParam(required = false) List<String> areas,
        @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable
    ){
        SearchResponse response = searchService.search(keyword,areas,pageable);
        return ResponseEntity.ok(ApiResponse.success("통합 검색이 완료되었습니다.",response));
    }

}
