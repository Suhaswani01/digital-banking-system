package com.bankingapp.kycservice.security;

public record AuthenticatedUser(Long userId, String email, String role) {
}
