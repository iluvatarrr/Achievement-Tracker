package ru.dmitriy.goalservice.web.mapper;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.goal.SubGoal;
import ru.dmitriy.goalservice.web.dto.SubGoalDto;
import ru.dmitriy.commondomain.util.Mappable;

@Service
public class SubGoalMapper implements Mappable<SubGoal, SubGoalDto> {
    @Override
    public SubGoal toEntity(SubGoalDto subGoalDto) {
        if (subGoalDto == null) {
            return null;
        }

        SubGoal subGoal = new SubGoal();
        subGoal.setId(subGoalDto.id());
        subGoal.setTitle(subGoalDto.title());
        subGoal.setDescription(subGoalDto.description());
        subGoal.setGoalStatus(subGoalDto.goalStatus());
        subGoal.setCreatedAt(subGoalDto.createdAt());
        subGoal.setCompletedAt(subGoalDto.completedAt());
        subGoal.setDeadline(subGoalDto.deadline());
        return subGoal;
    }

    @Override
    public SubGoalDto toDto(SubGoal subGoal) {
        if (subGoal == null) {
            return null;
        }
        return new SubGoalDto(
                subGoal.getId(),
                subGoal.getTitle(),
                subGoal.getDescription(),
                subGoal.getGoalStatus(),
                subGoal.getCreatedAt(),
                subGoal.getCompletedAt(),
                subGoal.getDeadline()
        );
    }
}
