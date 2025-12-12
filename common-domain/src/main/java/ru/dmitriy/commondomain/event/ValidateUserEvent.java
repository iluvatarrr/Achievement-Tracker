package ru.dmitriy.commondomain.event;


public record ValidateUserEvent(
        Long userId,
        String requestId
) {
}