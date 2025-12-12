package ru.dmitriy.commondomain.domain.exception;

public class GoalNotFoundException extends ResourseNotFoundException {
    public GoalNotFoundException(String message) {
        super(message);
    }
}
