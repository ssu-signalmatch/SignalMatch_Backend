package com.signalmatch_backend.match.consumer;

import com.signalmatch_backend.match.domain.enums.MatchStatus;
import com.signalmatch_backend.match.dto.MatchAcceptedEvent;
import com.signalmatch_backend.match.dto.MatchRequestedEvent;
import com.signalmatch_backend.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchEventConsumer {

    private final MatchRepository matchRepository;


    @KafkaHandler
    public void onRequested(MatchRequestedEvent event) {
        log.info("[match.events] requested: {}", event);
        matchRepository.findById(event.matchId()).ifPresent(match -> {
            match.setStatus(MatchStatus.valueOf(event.status()));
            matchRepository.save(match);
            log.info("Match {} -> {}", match.getMatchId(), match.getStatus());
        });
    }

    @KafkaHandler
    public void onAccepted(MatchAcceptedEvent event) {
        log.info("[match.events] accepted: {}", event);
        matchRepository.findById(event.matchId()).ifPresent(match -> {
            match.setStatus(MatchStatus.valueOf(event.status())); // ACCEPTED
            matchRepository.save(match);
            log.info("Match {} -> {}", match.getMatchId(), match.getStatus());
        });
    }
}