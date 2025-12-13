package ru.dmitriy.commondomain.domain.group;

public enum GroupRole {
    OWNER,      // Владелец - полный доступ
    MODERATOR,  // Модератор - может назначать цели
    MEMBER      // Участник - может выполнять назначенные цели
}
