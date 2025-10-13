package com.signalmatch_backend.match.consumer;

import com.signalmatch_backend.match.domain.enums.MatchStatus;
import com.signalmatch_backend.match.dto.MatchRequestedEvent;
import com.signalmatch_backend.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchRequestedConsumer {

    private final MatchRepository matchRepository;

    @KafkaListener(topics = "match.events", groupId = "match-service")
    public void onMessage(MatchRequestedEvent event) {
        log.info("[match.events] received: {}", event);

        matchRepository.findById(event.matchId()).ifPresent(match -> {
            // 데모 로직
            String status = event.status();
            match.setStatus(MatchStatus.valueOf(status));
            matchRepository.save(match);
            log.info("Match {} -> {}", match.getMatchId(), match.getStatus());
        });
    }
}