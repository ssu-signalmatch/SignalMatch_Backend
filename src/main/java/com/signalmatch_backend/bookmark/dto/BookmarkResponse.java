package com.signalmatch_backend.bookmark.dto;

public record BookmarkResponse(
    Long bookmarkId,
    Long userId,

    Long targetUserId

) {

}
