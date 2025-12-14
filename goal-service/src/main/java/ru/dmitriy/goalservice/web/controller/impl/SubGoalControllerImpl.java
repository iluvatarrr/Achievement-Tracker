package ru.dmitriy.goalservice.web.controller.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dmitriy.commondomain.domain.exception.GoalNotFoundException;
import ru.dmitriy.commondomain.domain.exception.SubGoalNotFountException;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.commondomain.domain.goal.SubGoal;
import ru.dmitriy.commondomain.util.MapperRegistry;
import ru.dmitriy.goalservice.service.SubGoalService;
import ru.dmitriy.goalservice.web.controller.SubGoalController;
import ru.dmitriy.goalservice.web.dto.goal.SubGoalDto;
import ru.dmitriy.goalservice.web.dto.goal.UpdateSubGoalDto;
import ru.dmitriy.commondomain.util.Mappable;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/sub-goals")
public class SubGoalControllerImpl implements SubGoalController {

    private final SubGoalService subGoalService;
    private final MapperRegistry mapperRegistry;

    public SubGoalControllerImpl(SubGoalService subGoalService, MapperRegistry mapperRegistry) {
        this.subGoalService = subGoalService;
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    @GetMapping("by-goal-id/{goalId}")
    @PreAuthorize("@customSecurityExpression.canAccessGoal(#goalId)")
    public List<SubGoalDto> getAllByGoalId(@Min(1) @PathVariable Long goalId) throws GoalNotFoundException {
        Mappable<SubGoal, SubGoalDto> mapper = mapperRegistry.get("subGoalMapper");
        List<SubGoal> subGoals = subGoalService.getAllByGoalId(goalId);
        return mapper.toDto(subGoals);
    }

    @Override
    @PatchMapping("/{subGoalId}/status")
    @PreAuthorize("@customSecurityExpression.canAccessSubGoal(#subGoalId)")
    public SubGoalDto updateStatus(@Min(1) @PathVariable Long subGoalId, @NotNull @RequestParam GoalStatus status
    ) throws SubGoalNotFountException {
        Mappable<SubGoal, SubGoalDto> mapper = mapperRegistry.get("subGoalMapper");
        SubGoal updatedSubGoal = subGoalService.updateStatus(subGoalId, status);
        return mapper.toDto(updatedSubGoal);
    }

    @Override
    @PatchMapping("/{subGoalId}")
    @PreAuthorize("@customSecurityExpression.canAccessSubGoal(#subGoalId)")
    public UpdateSubGoalDto update(@Min(1) @PathVariable Long subGoalId, @Valid @RequestBody UpdateSubGoalDto subGoalDto) throws SubGoalNotFountException {
        Mappable<SubGoal, UpdateSubGoalDto> mapper = mapperRegistry.get("updateSubGoalMapper");
        var subGoal = mapper.toEntity(subGoalDto);
        SubGoal updatedSubGoal = subGoalService.update(subGoalId, subGoal);
        return mapper.toDto(updatedSubGoal);
    }

    @Override
    @DeleteMapping("/{subGoalId}")
    @PreAuthorize("@customSecurityExpression.canAccessSubGoal(#subGoalId)")
    public void delete(@Min(1) @PathVariable Long subGoalId) throws SubGoalNotFountException {
        subGoalService.delete(subGoalId);
    }
}