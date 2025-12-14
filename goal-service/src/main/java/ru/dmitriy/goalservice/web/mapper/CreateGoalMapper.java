package ru.dmitriy.goalservice.web.mapper;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.goal.Goal;
import ru.dmitriy.commondomain.domain.goal.SubGoal;
import ru.dmitriy.goalservice.web.dto.goal.CreateGoalDto;
import ru.dmitriy.commondomain.util.Mappable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreateGoalMapper implements Mappable<Goal, CreateGoalDto> {

    private final CreateSubGoalMapper createSubGoalMapper;

    public CreateGoalMapper(CreateSubGoalMapper createSubGoalMapper) {
        this.createSubGoalMapper = createSubGoalMapper;
    }

    @Override
    public Goal toEntity(CreateGoalDto goalDto) {
        if (goalDto == null) {
            return null;
        }
        Goal goal = new Goal();
        goal.setTitle(goalDto.title());
        goal.setDescription(goalDto.description());
        goal.setGoalStatus(goalDto.goalStatus());
        goal.setGoalCategory(goalDto.goalCategory());
        goal.setGoalType(goalDto.goalType());
        goal.setDeadline(goalDto.deadline());
        if (goalDto.subGoalList() != null) {
            List<SubGoal> subGoals = goalDto.subGoalList().stream()
                    .map(subGoalDto -> {
                        SubGoal subGoal = createSubGoalMapper.toEntity(subGoalDto);
                        subGoal.setGoal(goal);
                        return subGoal;
                    })
                    .collect(Collectors.toList());
            goal.setSubGoalList(subGoals);
        }
        return goal;
    }

    @Override
    public CreateGoalDto toDto(Goal goal) {
        if (goal == null) {
            return null;
        }
        return new CreateGoalDto(
                goal.getTitle(),
                goal.getDescription(),
                goal.getGoalStatus(),
                goal.getGoalType(),
                goal.getGoalCategory(),
                goal.getDeadline(),
                goal.getSubGoalList() != null ?
                        goal.getSubGoalList().stream()
                                .map(createSubGoalMapper::toDto)
                                .collect(Collectors.toList()) :
                        List.of()
        );
    }
}
