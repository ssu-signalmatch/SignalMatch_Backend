package com.signalmatch_backend.match.dto;

import com.signalmatch_backend.match.domain.enums.MatchReasonCode;

public record CancelRequest(MatchReasonCode reasonCode, String reasonText) {}