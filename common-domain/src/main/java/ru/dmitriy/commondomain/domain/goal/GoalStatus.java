package ru.dmitriy.commondomain.domain.goal;

public enum GoalStatus {
    IN_PROGRESS, DONE, EXPIRED, REJECTED;

    public static GoalStatus getCreatedStatus() {
        return GoalStatus.IN_PROGRESS;
    }
}