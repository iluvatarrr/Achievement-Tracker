package ru.dmitriy.goalservice.web.controller.impl;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.dmitriy.commondomain.domain.exception.GoalNotFoundException;
import ru.dmitriy.commondomain.domain.exception.SubGoalNotFountException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.goal.Goal;
import ru.dmitriy.commondomain.domain.goal.GoalCategory;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.commondomain.domain.goal.SubGoal;
import ru.dmitriy.goalservice.service.GoalService;
import ru.dmitriy.goalservice.web.controller.GoalController;
import ru.dmitriy.goalservice.web.dto.CreateGoalDto;
import ru.dmitriy.goalservice.web.dto.CreateSubGoalDto;
import ru.dmitriy.goalservice.web.dto.GoalDto;
import ru.dmitriy.goalservice.web.dto.UpdateGoalDto;
import ru.dmitriy.goalservice.web.mapper.Mappable;
import ru.dmitriy.goalservice.web.mapper.MapperRegistry;
import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/goal")
public class GoalControllerImpl implements GoalController {

    private final GoalService goalService;
    private final MapperRegistry mapperRegistry;

    public GoalControllerImpl(GoalService goalService, MapperRegistry mapperRegistry) {
        this.goalService = goalService;
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    @GetMapping("/{id}")
    public GoalDto getById(@PathVariable Long id) throws GoalNotFoundException {
        Mappable<Goal, GoalDto> mapper = mapperRegistry.get("goalMapper");
        return mapper.toDto(goalService.getById(id));
    }

    @Override
    @GetMapping("filtered/{id}")
    public List<GoalDto> getAllFiltered(
            @RequestParam(required = false) GoalStatus status,
            @RequestParam(required = false) GoalCategory category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline,
            @PathVariable Long id
    ) throws UserNotFoundException, ServiceUnavailableException {
        Mappable<Goal, GoalDto> mapper = mapperRegistry.get("goalMapper");

        return goalService.findFiltered(status, category, deadline, id)
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    @Override
    @PostMapping()
    public Long create(@RequestBody CreateGoalDto goalDto, @RequestParam Long userId) throws UserNotFoundException, ServiceUnavailableException {
        Mappable<Goal, CreateGoalDto> mapper = mapperRegistry.get("createGoalMapper");
        var goal = mapper.toEntity(goalDto);
        return goalService.save(goal, userId);
    }

    @Override
    @PatchMapping("/{id}")
    public UpdateGoalDto update(@PathVariable Long id, @RequestBody UpdateGoalDto goalDto) throws GoalNotFoundException {
        Mappable<Goal, UpdateGoalDto> mapper = mapperRegistry.get("updateGoalMapper");
        var goal = mapper.toEntity(goalDto);
        return mapper.toDto(goalService.update(id,goal));
    }

    @Override
    @PatchMapping("/{id}/sub-goal/add")
    public GoalDto addSubGoal(@PathVariable Long id, @RequestBody CreateSubGoalDto subGoalDto) throws GoalNotFoundException {
        Mappable<SubGoal, CreateSubGoalDto> subGoalMapper = mapperRegistry.get("createSubGoalMapper");
        var subGoal = subGoalMapper.toEntity(subGoalDto);
        Mappable<Goal, GoalDto> goalMapper = mapperRegistry.get("goalMapper");
        return goalMapper.toDto(goalService.addSubGoal(id, subGoal));
    }

    @Override
    @PatchMapping("/{id}/sub-goal/remove/{subId}")
    public GoalDto removeSubGoal(@PathVariable Long id, @PathVariable Long subId) throws GoalNotFoundException, SubGoalNotFountException {
        Mappable<Goal, GoalDto> goalMapper = mapperRegistry.get("goalMapper");
        return goalMapper.toDto(goalService.removeSubGoal(id, subId));
    }

    @Override
    @PatchMapping("/status/{id}")
    public GoalDto updateStatusGoal(@PathVariable Long id, @RequestParam GoalStatus status) throws GoalNotFoundException {
        Mappable<Goal, GoalDto> goalMapper = mapperRegistry.get("goalMapper");
        return goalMapper.toDto(goalService.updateGoalStatus(id, status));
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        goalService.delete(id);
    }

    @Override
    public Double calculateGoalProgress(Long goalId) throws GoalNotFoundException {
        return 0.0;
    }
}
