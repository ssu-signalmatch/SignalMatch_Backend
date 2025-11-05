package com.signalmatch_backend.s3;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class S3KeyResolver {

    public String profileImageKey(Long userId, String ext) {
        return String.format("users/%d/profile/%s.%s", userId, UUID.randomUUID(), ext);
    }

    public String irFileKey(Long companyId, String ext) {
        LocalDate now = LocalDate.now();
        return String.format("companies/%d/ir/%04d/%02d/%02d/%s.%s",
                companyId, now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                UUID.randomUUID(), ext);
    }
}
