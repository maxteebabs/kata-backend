package com.cdl.kata.exceptions;

import java.time.Instant;
import java.util.List;

public class ValidationErrorResponse {
    private int status;
    private List<String> errors;
    private Instant timestamp;

    public ValidationErrorResponse(int status, List<String> errors) {
        this.status = status;
        this.errors = errors;
        this.timestamp = Instant.now();
    }

    public int getStatus() {
        return status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}