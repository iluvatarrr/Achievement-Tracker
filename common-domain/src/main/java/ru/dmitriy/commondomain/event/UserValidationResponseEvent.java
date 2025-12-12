package ru.dmitriy.commondomain.event;

public record UserValidationResponseEvent(
        Long userId,
        boolean exists,
        String requestId,
        String message
) {}