package com.signalmatch_backend.startup;

import com.signalmatch_backend.common.exception.CustomException;
import com.signalmatch_backend.common.exception.ErrorCode;
import com.signalmatch_backend.startup.domain.Startup;
import com.signalmatch_backend.startup.repository.StartupRepository;
import com.signalmatch_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupFinder {

    private final StartupRepository startupRepository;

    public Startup findByStartupId(Long startupId) {
        return startupRepository.findById(startupId)
                .orElseThrow(() -> new CustomException(ErrorCode.STARTUP_NOT_FOUND));
    }

    public Startup findByOwner(User user) {
        return startupRepository.findByOwner(user)
                .orElseThrow(() -> new CustomException(ErrorCode.STARTUP_NOT_FOUND));
    }
}
