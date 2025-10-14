package com.signalmatch_backend.match.service;

import com.signalmatch_backend.match.config.KafkaTopicsConfig;
import com.signalmatch_backend.match.dto.MatchAcceptedEvent;
import com.signalmatch_backend.match.dto.MatchRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchEventPublisher {


    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.enabled}")
    private boolean kafkaEnabled;

    public void publishMatchRequested(MatchRequestedEvent event) {
        publish(KafkaTopicsConfig.MATCH_EVENTS, event.investorId(), event);
    }

    public void publishMatchAccepted(MatchAcceptedEvent event) {
        publish(KafkaTopicsConfig.MATCH_EVENTS, event.investorId(), event);
    }

    // 공통 로직 분리
    private void publish(String topic, Long keyId, Object event) {
        if (!kafkaEnabled) {
            log.info("[NO-KAFKA] skip publish: {}", event);
            return;
        }

        String key = String.valueOf(keyId);
        kafkaTemplate.send(topic, key, event)
                .whenComplete((res, ex) -> {
                    if (ex != null) {
                        log.error("Kafka send failed: {}", event, ex);
                    } else {
                        log.info("Kafka sent topic={}, partition={}, offset={}, payload={}",
                                res.getRecordMetadata().topic(),
                                res.getRecordMetadata().partition(),
                                res.getRecordMetadata().offset(),
                                event);
                    }
                });
    }
}
