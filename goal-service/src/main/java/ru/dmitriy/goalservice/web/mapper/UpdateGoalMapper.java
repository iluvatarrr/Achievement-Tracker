package ru.dmitriy.goalservice.web.mapper;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.goal.Goal;
import ru.dmitriy.commondomain.domain.goal.SubGoal;
import ru.dmitriy.goalservice.web.dto.UpdateGoalDto;
import ru.dmitriy.commondomain.util.Mappable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpdateGoalMapper implements Mappable<Goal, UpdateGoalDto> {

    private final UpdateSubGoalMapper updateSubGoalMapper;

    public UpdateGoalMapper(UpdateSubGoalMapper updateSubGoalMapper) {
        this.updateSubGoalMapper = updateSubGoalMapper;
    }

    @Override
    public Goal toEntity(UpdateGoalDto goalDto) {
        if (goalDto == null) {
            return null;
        }
        Goal goal = new Goal();
        goal.setId(goalDto.id());
        goal.setTitle(goalDto.title());
        goal.setDescription(goalDto.description());
        goal.setGoalStatus(goalDto.goalStatus());
        goal.setGoalCategory(goalDto.goalCategory());
        goal.setDeadline(goalDto.deadline());
        if (goalDto.subGoalList() != null) {
            List<SubGoal> subGoals = goalDto.subGoalList().stream()
                    .map(subGoalDto -> {
                        SubGoal subGoal = updateSubGoalMapper.toEntity(subGoalDto);
                        subGoal.setGoal(goal);
                        return subGoal;
                    })
                    .collect(Collectors.toList());
            goal.setSubGoalList(subGoals);
        }
        return goal;
    }

    @Override
    public UpdateGoalDto toDto(Goal goal) {
        if (goal == null) {
            return null;
        }
        return new UpdateGoalDto(
                goal.getId(),
                goal.getTitle(),
                goal.getDescription(),
                goal.getGoalStatus(),
                goal.getGoalCategory(),
                goal.getDeadline(),
                goal.getSubGoalList() != null ?
                        goal.getSubGoalList().stream()
                                .map(updateSubGoalMapper::toDto)
                                .collect(Collectors.toList()) :
                        List.of()
        );
    }
}
