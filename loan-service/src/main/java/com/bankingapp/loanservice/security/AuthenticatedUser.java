package com.bankingapp.loanservice.security;

public record AuthenticatedUser(Long userId, String email, String role) {
}
