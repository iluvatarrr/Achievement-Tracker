package ru.dmitriy.goalservice.web.controller.impl;

import jakarta.validation.constraints.Min;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dmitriy.goalservice.service.impl.StatisticsService;
import ru.dmitriy.goalservice.web.controller.StatisticsController;
import ru.dmitriy.goalservice.web.dto.statictics.CategoryStatisticsDto;
import ru.dmitriy.goalservice.web.dto.statictics.OverallStatisticsDto;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsControllerImpl implements StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsControllerImpl(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Override
    @GetMapping("/user/{userId}/overall")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userId)")
    public OverallStatisticsDto getUserOverallStatistics(@Min(1) @PathVariable Long userId) {
        return statisticsService.getOverallStatistics(userId);
    }

    @Override
    @GetMapping("/user/{userId}/categories")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userId)")
    public List<CategoryStatisticsDto> getUserStatisticsByCategory(@Min(1) @PathVariable Long userId) {
        return statisticsService.getStatisticsByCategory(userId);
    }

    @Override
    @GetMapping("/group/{groupId}/overall")
    @PreAuthorize("@customSecurityExpression.canAccessGroup(#groupId)")
    public OverallStatisticsDto getGroupStatistics(@Min(1) @PathVariable Long groupId) {
        return statisticsService.getGroupStatistics(groupId);
    }
}
