package ru.dmitriy.goalservice.web.controller.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dmitriy.commondomain.domain.exception.GoalNotFoundException;
import ru.dmitriy.commondomain.domain.exception.SubGoalNotFountException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.goal.Goal;
import ru.dmitriy.commondomain.domain.goal.GoalCategory;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.commondomain.domain.goal.SubGoal;
import ru.dmitriy.commondomain.util.MapperRegistry;
import ru.dmitriy.goalservice.service.GoalService;
import ru.dmitriy.goalservice.web.controller.GoalController;
import ru.dmitriy.goalservice.web.dto.CreateGoalDto;
import ru.dmitriy.goalservice.web.dto.CreateSubGoalDto;
import ru.dmitriy.goalservice.web.dto.GoalDto;
import ru.dmitriy.goalservice.web.dto.UpdateGoalDto;
import ru.dmitriy.commondomain.util.Mappable;
import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.List;

@Validated
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
    @PreAuthorize("@customSecurityExpression.canAccessGoal(#id)")
    public GoalDto getById(@Min(1) @PathVariable Long id) throws GoalNotFoundException {
        Mappable<Goal, GoalDto> mapper = mapperRegistry.get("goalMapper");
        return mapper.toDto(goalService.getById(id));
    }

    @Override
    @GetMapping("filtered/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public List<GoalDto> getAllFiltered(
            @RequestParam(required = false) GoalStatus status,
            @RequestParam(required = false) GoalCategory category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline,
            @Min(1) @PathVariable Long id
    ) throws UserNotFoundException, ServiceUnavailableException {
        Mappable<Goal, GoalDto> mapper = mapperRegistry.get("goalMapper");

        return goalService.findFiltered(status, category, deadline, id)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @PostMapping
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userId)")
    public Long create(@Valid @RequestBody CreateGoalDto goalDto, @Min(1) @RequestParam Long userId) throws UserNotFoundException, ServiceUnavailableException {
        Mappable<Goal, CreateGoalDto> mapper = mapperRegistry.get("createGoalMapper");
        var goal = mapper.toEntity(goalDto);
        return goalService.save(goal, userId);
    }

    @Override
    @PostMapping("/create/group")
    @PreAuthorize("@customSecurityExpression.canManageMember(#groupId)")
    public Long createGroupGoal(@Valid @RequestBody CreateGoalDto goalDto, @Min(1) @RequestParam Long userId, @Min(1) @RequestParam Long groupId) throws UserNotFoundException, ServiceUnavailableException {
        Mappable<Goal, CreateGoalDto> mapper = mapperRegistry.get("createGoalMapper");
        var goal = mapper.toEntity(goalDto);
        return goalService.save(goal, userId, groupId);
    }

    @Override
    @PatchMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessGoal(#id)")
    public UpdateGoalDto update(@Min(1) @PathVariable Long id, @Valid @RequestBody UpdateGoalDto goalDto) throws GoalNotFoundException {
        Mappable<Goal, UpdateGoalDto> mapper = mapperRegistry.get("updateGoalMapper");
        var goal = mapper.toEntity(goalDto);
        return mapper.toDto(goalService.update(id,goal));
    }

    @Override
    @PatchMapping("/{id}/sub-goal/add")
    @PreAuthorize("@customSecurityExpression.canAccessGoal(#id)")
    public GoalDto addSubGoal(@Min(1) @PathVariable Long id, @Valid @RequestBody CreateSubGoalDto subGoalDto) throws GoalNotFoundException {
        Mappable<SubGoal, CreateSubGoalDto> subGoalMapper = mapperRegistry.get("createSubGoalMapper");
        var subGoal = subGoalMapper.toEntity(subGoalDto);
        Mappable<Goal, GoalDto> goalMapper = mapperRegistry.get("goalMapper");
        return goalMapper.toDto(goalService.addSubGoal(id, subGoal));
    }

    @Override
    @PatchMapping("/{id}/sub-goal/remove/{subId}")
    @PreAuthorize("@customSecurityExpression.canAccessGoal(#id)")
    public GoalDto removeSubGoal(@Min(1) @PathVariable Long id, @Min(1) @PathVariable Long subId) throws GoalNotFoundException, SubGoalNotFountException {
        Mappable<Goal, GoalDto> goalMapper = mapperRegistry.get("goalMapper");
        return goalMapper.toDto(goalService.removeSubGoal(id, subId));
    }

    @Override
    @PatchMapping("/status/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessGoal(#id)")
    public GoalDto updateStatusGoal(@Min(1) @PathVariable Long id, @NotNull @RequestParam GoalStatus status) throws GoalNotFoundException {
        Mappable<Goal, GoalDto> goalMapper = mapperRegistry.get("goalMapper");
        return goalMapper.toDto(goalService.updateGoalStatus(id, status));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessGoal(#id)")
    public void delete(@Min(1) @PathVariable Long id) {
        goalService.delete(id);
    }
}
