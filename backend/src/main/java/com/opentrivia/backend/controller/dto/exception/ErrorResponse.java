package com.opentrivia.backend.controller.dto.exception;

public record ErrorResponse(
        int status,
        String message
) {}
