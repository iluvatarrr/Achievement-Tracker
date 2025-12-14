package ru.dmitriy.goalservice.web.dto.statictics;

import ru.dmitriy.commondomain.domain.goal.GoalCategory;

public record CategoryStatisticsDto(
        GoalCategory category,
        int totalGoals,
        int doneGoals,
        double averageProgress
) {}