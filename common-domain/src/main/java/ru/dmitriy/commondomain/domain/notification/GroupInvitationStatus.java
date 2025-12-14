package ru.dmitriy.commondomain.domain.notification;

public enum GroupInvitationStatus {
    PENDING,    // Ожидает ответа
    ACCEPTED,   // Принято
    DECLINED,   // Отклонено
    EXPIRED,    // Истекло
    CANCELLED   // Отменено
}