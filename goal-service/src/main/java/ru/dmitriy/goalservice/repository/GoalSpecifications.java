package ru.dmitriy.goalservice.repository;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import ru.dmitriy.commondomain.domain.goal.Goal;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.commondomain.domain.goal.GoalCategory;

import java.time.LocalDateTime;

public abstract class GoalSpecifications {

    public static Specification<Goal> hasStatus(GoalStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("goalStatus"), status);
    }

    public static Specification<Goal> fetchSubGoals() {
        return (root, query, cb) -> {
            root.fetch("subGoalList", JoinType.LEFT);
            return null;
        };
    }

    public static Specification<Goal> hasCategory(GoalCategory category) {
        return (root, query, cb) ->
                category == null ? null : cb.equal(root.get("goalCategory"), category);
    }

    public static Specification<Goal> deadlineBefore(LocalDateTime deadline) {
        return (root, query, cb) ->
                deadline == null ? null : cb.lessThanOrEqualTo(root.get("deadline"), deadline);
    }
}