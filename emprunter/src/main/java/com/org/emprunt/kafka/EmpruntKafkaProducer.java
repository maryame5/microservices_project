package com.org.emprunt.kafka;

import com.org.emprunt.event.EmpruntCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class EmpruntKafkaProducer {

    private final KafkaTemplate<String, EmpruntCreatedEvent> kafkaTemplate;
    private static final String TOPIC = "emprunt-created";

    public EmpruntKafkaProducer(KafkaTemplate<String, EmpruntCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmpruntCreatedEvent(Long empruntId, Long userId, Long bookId) {
        EmpruntCreatedEvent event = new EmpruntCreatedEvent();
        event.setEmpruntId(empruntId);
        event.setUserId(userId);
        event.setBookId(bookId);
        event.setCreatedAt(LocalDateTime.now());
        event.setMessage("Nouvel emprunt créé: Utilisateur " + userId + " a emprunté le livre " + bookId);

        kafkaTemplate.send(TOPIC, String.valueOf(empruntId), event);
    }
}
