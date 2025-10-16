package com.signalmatch_backend.match.dto;

import com.signalmatch_backend.match.domain.enums.MatchReasonCode;

public record RejectRequest(MatchReasonCode reasonCode, String reasonText) {}
