package ru.dmitriy.commondomain.domain.exception;

public class UserNotFoundException extends ResourseNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
