package ru.dmitriy.goalservice.web.dto.statictics;

public record OverallStatisticsDto(
        int totalGoals,
        int doneGoals,
        double averageProgress
) {}