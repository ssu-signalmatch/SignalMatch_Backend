package com.signalmatch_backend.match.consumer;

import com.signalmatch_backend.match.config.KafkaTopicsConfig;
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
@KafkaListener(
        topics = KafkaTopicsConfig.MATCH_EVENTS,
        groupId = "match-group",
        containerFactory = "kafkaJsonListenerFactory"
)
public class MatchEventConsumer {

    private final MatchRepository matchRepository;


    @KafkaHandler
    public void handleRequestedEvent(MatchRequestedEvent event) {
        log.info("[match.events] received MatchRequestedEvent: {}", event);

        MatchStatus status = MatchStatus.valueOf(event.status());

        switch (status) {
            case REQUESTED -> handleRequested(event);
            case REJECTED -> handleRejected(event);
            case CANCELLED  -> handleCanceled(event);
            default -> log.warn("Unknown MatchStatus: {}", status);
        }
    }

    private void handleRequested(MatchRequestedEvent event) {
        log.info("[match.events] requested: {}", event);
        matchRepository.findById(event.matchId()).ifPresent(match -> {
            match.setStatus(MatchStatus.REQUESTED);
            matchRepository.save(match);
            log.info("Match {} -> {}", match.getMatchId(), match.getStatus());
        });
    }

    private void handleRejected(MatchRequestedEvent event) {
        log.info("[match.events] rejected: {}", event);
        matchRepository.findById(event.matchId()).ifPresent(match -> {
            match.setStatus(MatchStatus.REJECTED);
            matchRepository.save(match);
            log.info("Match {} -> {}", match.getMatchId(), match.getStatus());
        });
    }

    private void handleCanceled(MatchRequestedEvent event) {
        log.info("[match.events] canceled: {}", event);
        matchRepository.findById(event.matchId()).ifPresent(match -> {
            match.setStatus(MatchStatus.CANCELLED);
            matchRepository.save(match);
            log.info("Match {} -> {}", match.getMatchId(), match.getStatus());
        });
    }

    @KafkaHandler
    public void onAccepted(MatchAcceptedEvent event) {
        log.info("[match.events] accepted: {}", event);
        matchRepository.findById(event.matchId()).ifPresent(match -> {
            match.setStatus(MatchStatus.valueOf(event.status())); // 일반적으로 ACCEPTED
            matchRepository.save(match);
            log.info("Match {} -> {}", match.getMatchId(), match.getStatus());
        });
    }
}