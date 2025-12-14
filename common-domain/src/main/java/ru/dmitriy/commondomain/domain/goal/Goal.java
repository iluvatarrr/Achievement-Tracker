package ru.dmitriy.commondomain.domain.goal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ru.dmitriy.commondomain.domain.group.Group;
import ru.dmitriy.commondomain.domain.user.User;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "goals")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private GoalStatus goalStatus;
    @Enumerated(EnumType.STRING)
    private GoalCategory goalCategory;
    @Enumerated(EnumType.STRING)
    private GoalType goalType;
    private Integer progressInPercent;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime completedAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadline;
    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SubGoal> subGoalList = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @JsonIgnore
    private Group group;

    public Goal(Long id, String title, String description, GoalStatus goalStatus, GoalCategory goalCategory,
                LocalDateTime createdAt, LocalDateTime completedAt, LocalDateTime deadline, List<SubGoal> subGoalList,
                User user, Group group, GoalType goalType) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.goalStatus = goalStatus;
        this.goalCategory = goalCategory;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.deadline = deadline;
        this.subGoalList = subGoalList;
        this.user = user;
        this.group = group;
        this.goalType = goalType;
    }

    public Goal() {}

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

    public GoalCategory getGoalCategory() {
        return goalCategory;
    }

    public void setGoalCategory(GoalCategory goalCategory) {
        this.goalCategory = goalCategory;
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

    public List<SubGoal> getSubGoalList() {
        return subGoalList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSubGoalList(List<SubGoal> subGoals) {
        this.subGoalList.clear();
        if (subGoals != null) {
            for (SubGoal subGoal : subGoals) {
                addSubGoal(subGoal);
            }
        }
    }

    public Integer getProgressInPercent() {
        return progressInPercent;
    }

    public void setProgressInPercent(Integer progressInPercent) {
        this.progressInPercent = progressInPercent;
    }

    public void addSubGoal(SubGoal subGoal) {
        subGoalList.add(subGoal);
        subGoal.setGoal(this);
        if (subGoal.getCreatedAt() == null) {
            subGoal.setCreatedAt(LocalDateTime.now());
        }
    }

    public void removeSubGoal(SubGoal subGoal) {
        subGoalList.remove(subGoal);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroups(Group group) {
        this.group = group;
    }

    public GoalType getGoalType() {
        return goalType;
    }

    public void setGoalType(GoalType goalType) {
        this.goalType = goalType;
    }
}