package com.signalmatch_backend.match.service;

import com.signalmatch_backend.match.domain.Match;
import com.signalmatch_backend.match.domain.enums.MatchStatus;
import com.signalmatch_backend.match.dto.MatchRequestedEvent;
import com.signalmatch_backend.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchEventPublisher publisher;

    public Long createMatch(Long startupId, Long investorId) {
        Match match = Match.builder()
                .startupId(startupId)
                .investorId(investorId)
                .status(MatchStatus.REQUESTED)
                .build();
        match = matchRepository.save(match);

        // 2) 이벤트 발행
        publisher.publishMatchRequested(new MatchRequestedEvent(
                match.getMatchId(),
                match.getStartupId(),
                match.getInvestorId(),
                match.getStatus().name(),
                LocalDateTime.now()
        ));
        return match.getMatchId();
    }

}
