package com.bankingapp.notificationservice.service;

import com.bankingapp.notificationservice.dto.NotificationResponse;
import com.bankingapp.notificationservice.dto.SendNotificationRequest;
import com.bankingapp.notificationservice.entity.Notification;
import com.bankingapp.notificationservice.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationResponse send(SendNotificationRequest request) {

        // Simulated delivery: in a real system this would call an email/SMS provider.
        log.info("Sending notification to user {}: [{}] {}",
                request.getUserId(), request.getType(), request.getMessage());

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .message(request.getMessage())
                .build();

        return toResponse(notificationRepository.save(notification));
    }

    public List<NotificationResponse> getMyNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .type(n.getType())
                .message(n.getMessage())
                .createdAt(n.getCreatedAt())
                .build();
    }
}