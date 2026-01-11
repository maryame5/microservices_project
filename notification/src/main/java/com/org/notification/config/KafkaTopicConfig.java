package com.org.notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic empruntCreatedTopic() {
        return new NewTopic("emprunt-created", 1, (short) 1);
    }
}
