package com.signalmatch_backend.bookmark.dto;

import jakarta.validation.constraints.NotNull;

public record BookmarkRequest(
    Long investorId,

    Long startupId

) {

}
