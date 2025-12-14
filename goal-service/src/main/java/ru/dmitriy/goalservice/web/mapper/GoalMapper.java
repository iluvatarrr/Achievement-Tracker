package ru.dmitriy.goalservice.web.mapper;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.goal.Goal;
import ru.dmitriy.commondomain.domain.goal.SubGoal;
import ru.dmitriy.goalservice.web.dto.GoalDto;
import ru.dmitriy.commondomain.util.Mappable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalMapper implements Mappable<Goal, GoalDto> {

    private final SubGoalMapper subGoalMapper;

    public GoalMapper(SubGoalMapper subGoalMapper) {
        this.subGoalMapper = subGoalMapper;
    }

    @Override
    public Goal toEntity(GoalDto goalDto) {
        if (goalDto == null) {
            return null;
        }
        Goal goal = new Goal();
        goal.setId(goalDto.id());
        goal.setTitle(goalDto.title());
        goal.setDescription(goalDto.description());
        goal.setGoalStatus(goalDto.goalStatus());
        goal.setGoalCategory(goalDto.goalCategory());
        goal.setGoalType(goalDto.goalType());
        goal.setCreatedAt(goalDto.createdAt());
        goal.setCompletedAt(goalDto.completedAt());
        goal.setDeadline(goalDto.deadline());
        goal.setProgressInPercent(goalDto.progressInPercent());
        if (goalDto.subGoalList() != null) {
            List<SubGoal> subGoals = goalDto.subGoalList().stream()
                    .map(subGoalDto -> {
                        SubGoal subGoal = subGoalMapper.toEntity(subGoalDto);
                        subGoal.setGoal(goal);
                        return subGoal;
                    })
                    .collect(Collectors.toList());
            goal.setSubGoalList(subGoals);
        }
        return goal;
    }

    @Override
    public GoalDto toDto(Goal goal) {
        if (goal == null) {
            return null;
        }
        return new GoalDto(
                goal.getId(),
                goal.getTitle(),
                goal.getDescription(),
                goal.getGoalStatus(),
                goal.getGoalType(),
                goal.getGoalCategory(),
                goal.getProgressInPercent(),
                goal.getCreatedAt(),
                goal.getCompletedAt(),
                goal.getDeadline(),
                goal.getSubGoalList() != null ?
                        goal.getSubGoalList().stream()
                                .map(subGoalMapper::toDto)
                                .collect(Collectors.toList()) :
                        List.of()
        );
    }
}
