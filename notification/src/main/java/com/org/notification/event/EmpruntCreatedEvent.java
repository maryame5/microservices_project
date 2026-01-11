package com.org.notification.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpruntCreatedEvent implements Serializable {
    private Long empruntId;
    private Long userId;
    private Long bookId;
    private LocalDateTime createdAt;
    private String message;
}
