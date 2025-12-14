package ru.dmitriy.goalservice.web.controller;

import jakarta.validation.constraints.Min;
import ru.dmitriy.goalservice.web.dto.statictics.CategoryStatisticsDto;
import ru.dmitriy.goalservice.web.dto.statictics.OverallStatisticsDto;
import java.util.List;

public interface StatisticsController {
    OverallStatisticsDto getGroupStatistics(@Min(1) Long groupId);
    List<CategoryStatisticsDto> getUserStatisticsByCategory(@Min(1) Long userId);
    OverallStatisticsDto getUserOverallStatistics(@Min(1) Long userId);
}
