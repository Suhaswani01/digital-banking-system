package com.bankingapp.notificationservice.dto;

import com.bankingapp.notificationservice.entity.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationResponse {
    private Long id;
    private NotificationType type;
    private String message;
    private LocalDateTime createdAt;
}