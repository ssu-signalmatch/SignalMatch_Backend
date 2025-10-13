package com.signalmatch_backend.match.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicsConfig {
    public static final String MATCH_EVENTS="match.events";

    @Bean
    public NewTopic matchRequestsTopic() {
        // partitions=3, replicationFactor=1
        return new NewTopic(MATCH_EVENTS, 3, (short) 1);
    }
}
