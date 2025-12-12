package ru.dmitriy.commondomain.domain.goal;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sub_goals")
public class SubGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private GoalStatus goalStatus;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private LocalDateTime deadline;
    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    public SubGoal(Long id, String title, String description, GoalStatus goalStatus, LocalDateTime createdAt, LocalDateTime completedAt, LocalDateTime deadline, Goal goal) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.goalStatus = goalStatus;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.deadline = deadline;
        this.goal = goal;
    }

    public SubGoal() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GoalStatus getGoalStatus() {
        return goalStatus;
    }

    public void setGoalStatus(GoalStatus goalStatus) {
        this.goalStatus = goalStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SubGoal subGoal = (SubGoal) object;
        return Objects.equals(id, subGoal.id) && Objects.equals(title, subGoal.title) && Objects.equals(description, subGoal.description) && goalStatus == subGoal.goalStatus && Objects.equals(createdAt, subGoal.createdAt) && Objects.equals(completedAt, subGoal.completedAt) && Objects.equals(deadline, subGoal.deadline) && Objects.equals(goal, subGoal.goal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, goalStatus, createdAt, completedAt, deadline, goal);
    }
}
