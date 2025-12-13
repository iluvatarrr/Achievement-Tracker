package ru.dmitriy.goalservice.web.controller.impl;

import org.springframework.web.bind.annotation.*;
import ru.dmitriy.commondomain.domain.exception.GoalNotFoundException;
import ru.dmitriy.commondomain.domain.exception.SubGoalNotFountException;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.commondomain.domain.goal.SubGoal;
import ru.dmitriy.commondomain.util.MapperRegistry;
import ru.dmitriy.goalservice.service.SubGoalService;
import ru.dmitriy.goalservice.web.controller.SubGoalController;
import ru.dmitriy.goalservice.web.dto.SubGoalDto;
import ru.dmitriy.goalservice.web.dto.UpdateSubGoalDto;
import ru.dmitriy.commondomain.util.Mappable;
import java.util.List;

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
    public List<SubGoalDto> getAllByGoalId(@PathVariable Long goalId) throws GoalNotFoundException {
        Mappable<SubGoal, SubGoalDto> mapper = mapperRegistry.get("subGoalMapper");
        List<SubGoal> subGoals = subGoalService.getAllByGoalId(goalId);
        return mapper.toDto(subGoals);
    }

    @Override
    @PatchMapping("/{subGoalId}/status")
    public SubGoalDto updateStatus(@PathVariable Long subGoalId, @RequestParam GoalStatus status
    ) throws SubGoalNotFountException {
        Mappable<SubGoal, SubGoalDto> mapper = mapperRegistry.get("subGoalMapper");
        SubGoal updatedSubGoal = subGoalService.updateStatus(subGoalId, status);
        return mapper.toDto(updatedSubGoal);
    }

    @Override
    @PatchMapping("/{subGoalId}")
    public UpdateSubGoalDto update(@PathVariable Long subGoalId, @RequestBody UpdateSubGoalDto subGoalDto) throws SubGoalNotFountException {
        Mappable<SubGoal, UpdateSubGoalDto> mapper = mapperRegistry.get("updateSubGoalMapper");
        var subGoal = mapper.toEntity(subGoalDto);
        SubGoal updatedSubGoal = subGoalService.update(subGoalId, subGoal);
        return mapper.toDto(updatedSubGoal);
    }

    @Override
    @DeleteMapping("/{subGoalId}")
    public void delete(@PathVariable Long subGoalId) throws SubGoalNotFountException {
        subGoalService.delete(subGoalId);
    }
}