package com.signalmatch_backend.bookmark.dto;

import com.signalmatch_backend.bookmark.domain.Bookmark;
import com.signalmatch_backend.investor.domain.Investor;
import com.signalmatch_backend.startup.domain.Startup;
import lombok.Builder;

@Builder
public record BookmarkListResponse(
    String targetType,
    Long targetTypeId,
    Long targetUserId,
    String name

) {
}