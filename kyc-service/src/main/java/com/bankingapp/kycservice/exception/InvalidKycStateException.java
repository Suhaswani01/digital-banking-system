package com.bankingapp.kycservice.exception;

public class InvalidKycStateException extends RuntimeException {
    public InvalidKycStateException(String message) { super(message); }
}