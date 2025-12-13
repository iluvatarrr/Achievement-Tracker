package ru.dmitriy.commondomain.listener.event;


public record ValidateUserEvent(
        Long userId,
        String requestId
) {
}