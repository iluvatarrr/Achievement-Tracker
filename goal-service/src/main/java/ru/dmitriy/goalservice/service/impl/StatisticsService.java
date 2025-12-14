package ru.dmitriy.goalservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitriy.commondomain.domain.goal.Goal;
import ru.dmitriy.commondomain.domain.goal.GoalCategory;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.goalservice.service.GoalService;
import ru.dmitriy.goalservice.web.dto.statictics.CategoryStatisticsDto;
import ru.dmitriy.goalservice.web.dto.statictics.OverallStatisticsDto;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final GoalService goalService;

    public StatisticsService(GoalService goalService) {
        this.goalService = goalService;
    }

    @Transactional(readOnly = true)
    public OverallStatisticsDto getOverallStatistics(Long userId) {
        List<Goal> goals = goalService.findAllByUserId(userId);

        int totalGoals = goals.size();
        int doneGoals = (int) goals.stream().filter(g -> g.getGoalStatus() == GoalStatus.DONE).count();
        double avgProgress = goals.stream()
                .mapToInt(g -> Optional.ofNullable(g.getProgressInPercent()).orElse(0))
                .average()
                .orElse(0);

        return new OverallStatisticsDto(totalGoals, doneGoals, avgProgress);
    }

    @Transactional(readOnly = true)
    public List<CategoryStatisticsDto> getStatisticsByCategory(Long userId) {
        List<Goal> goals = goalService.findAllByUserId(userId);

        Map<GoalCategory, List<Goal>> grouped = goals.stream()
                .filter(g -> g.getGoalCategory() != null)
                .collect(Collectors.groupingBy(Goal::getGoalCategory));

        List<CompletableFuture<CategoryStatisticsDto>> futures = grouped.entrySet().stream()
                .map(entry -> CompletableFuture.supplyAsync(() -> {
                    List<Goal> catGoals = entry.getValue();
                    int total = catGoals.size();
                    int done = (int) catGoals.stream()
                            .filter(g -> g.getGoalStatus() == GoalStatus.DONE)
                            .count();
                    double avgProgress = catGoals.stream()
                            .mapToInt(g -> Optional.ofNullable(g.getProgressInPercent()).orElse(0))
                            .average()
                            .orElse(0);
                    return new CategoryStatisticsDto(entry.getKey(), total, done, avgProgress);
                }))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    @Transactional(readOnly = true)
    public OverallStatisticsDto getGroupStatistics(Long groupId) {
        List<Goal> goals = goalService.getAllByGroupId(groupId);

        int totalGoals = goals.size();
        int doneGoals = (int) goals.stream().filter(g -> g.getGoalStatus() == GoalStatus.DONE).count();
        double avgProgress = goals.stream()
                .mapToInt(g -> Optional.ofNullable(g.getProgressInPercent()).orElse(0))
                .average()
                .orElse(0);

        return new OverallStatisticsDto(totalGoals, doneGoals, avgProgress);
    }
}

