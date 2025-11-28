package com.signalmatch_backend.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StartupBookmarkInfo {
    private Long startupId;
    private String startupName;
    private String intro;
    private Long bookmarkCount;
}