package ru.dmitriy.commondomain.domain.exception;

public abstract class ResourseNotFoundException extends Exception {
    public ResourseNotFoundException(String message) {
        super(message);
    }
}
