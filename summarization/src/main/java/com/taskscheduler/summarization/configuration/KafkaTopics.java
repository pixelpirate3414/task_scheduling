package com.taskscheduler.summarization.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka")
public record KafkaTopics(Topics topics) {

    public record Topics(Rpc rpc) {}

    public record Rpc(
            String summarizationRequest,
            String summarizationResponse
    ) {}
}
