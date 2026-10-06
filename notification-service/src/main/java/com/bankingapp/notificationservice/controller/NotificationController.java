package com.bankingapp.notificationservice.controller;

import com.bankingapp.notificationservice.dto.NotificationResponse;
import com.bankingapp.notificationservice.dto.SendNotificationRequest;
import com.bankingapp.notificationservice.security.AuthenticatedUser;
import com.bankingapp.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Called internally by other services (Account, Transaction, Loan, KYC)
    @PostMapping("/internal/send")
    public ResponseEntity<NotificationResponse> send(@Valid @RequestBody SendNotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.send(request));
    }

    @GetMapping("/me")
    public ResponseEntity<List<NotificationResponse>> myNotifications(
            @AuthenticationPrincipal AuthenticatedUser user) {

        return ResponseEntity.ok(notificationService.getMyNotifications(user.userId()));
    }
}