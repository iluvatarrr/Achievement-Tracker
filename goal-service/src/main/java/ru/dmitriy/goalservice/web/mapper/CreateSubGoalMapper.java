package ru.dmitriy.goalservice.web.mapper;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.goal.SubGoal;
import ru.dmitriy.goalservice.web.dto.CreateSubGoalDto;
import ru.dmitriy.commondomain.util.Mappable;

@Service
public class CreateSubGoalMapper implements Mappable<SubGoal, CreateSubGoalDto> {
    @Override
    public SubGoal toEntity(CreateSubGoalDto subGoalDto) {
        if (subGoalDto == null) {
            return null;
        }
        SubGoal subGoal = new SubGoal();
        subGoal.setTitle(subGoalDto.title());
        subGoal.setDescription(subGoalDto.description());
        subGoal.setDeadline(subGoalDto.deadline());
        return subGoal;
    }

    @Override
    public CreateSubGoalDto toDto(SubGoal subGoal) {
        if (subGoal == null) {
            return null;
        }
        return new CreateSubGoalDto(
                subGoal.getTitle(),
                subGoal.getDescription(),
                subGoal.getDeadline()
        );
    }
}
