package ru.dmitriy.commondomain.listener.event;

public record UserValidationResponseEvent(
        Long userId,
        boolean exists,
        String requestId,
        String message
) {}