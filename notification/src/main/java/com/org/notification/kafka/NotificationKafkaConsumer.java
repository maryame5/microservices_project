package com.org.notification.kafka;

import com.org.notification.event.EmpruntCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationKafkaConsumer.class);

    @KafkaListener(topics = "emprunt-created", groupId = "notification-group")
    public void consumeEmpruntCreatedEvent(EmpruntCreatedEvent event) {
        logger.info("========================================");
        logger.info("📬 NOUVELLE NOTIFICATION REÇUE");
        logger.info("========================================");
        logger.info("ID Emprunt: {}", event.getEmpruntId());
        logger.info("ID Utilisateur: {}", event.getUserId());
        logger.info("ID Livre: {}", event.getBookId());
        logger.info("Date Création: {}", event.getCreatedAt());
        logger.info("Message: {}", event.getMessage());
        logger.info("========================================");

        // Vous pouvez ici ajouter la logique pour:
        // - Envoyer un email
        // - Envoyer un SMS
        // - Sauvegarder dans une base de données de notifications
        // - Envoyer une notification push
    }
}
