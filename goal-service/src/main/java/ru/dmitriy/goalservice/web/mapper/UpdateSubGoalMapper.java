package ru.dmitriy.goalservice.web.mapper;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.goal.SubGoal;
import ru.dmitriy.goalservice.web.dto.UpdateSubGoalDto;
import ru.dmitriy.commondomain.util.Mappable;

@Service
public class UpdateSubGoalMapper implements Mappable<SubGoal, UpdateSubGoalDto> {
    @Override
    public SubGoal toEntity(UpdateSubGoalDto subGoalDto) {
        if (subGoalDto == null) {
            return null;
        }

        SubGoal subGoal = new SubGoal();
        subGoal.setTitle(subGoalDto.title());
        subGoal.setDescription(subGoalDto.description());
        subGoal.setGoalStatus(subGoalDto.goalStatus());
        subGoal.setDeadline(subGoalDto.deadline());
        return subGoal;
    }

    @Override
    public UpdateSubGoalDto toDto(SubGoal subGoal) {
        if (subGoal == null) {
            return null;
        }
        return new UpdateSubGoalDto(
                subGoal.getTitle(),
                subGoal.getDescription(),
                subGoal.getGoalStatus(),
                subGoal.getDeadline()
                );
    }
}
