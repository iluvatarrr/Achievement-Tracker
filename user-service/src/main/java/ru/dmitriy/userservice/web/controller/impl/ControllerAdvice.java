package ru.dmitriy.userservice.web.controller.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.dmitriy.commondomain.domain.exception.*;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler({
            UserNotFoundException.class,
            GoalNotFoundException.class,
            GroupInvocationNotFoundException.class,
            GroupNotFoundException.class,
            SubGoalNotFountException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handleAllResourceNotFoundExceptions(Exception e) {
        return new ExceptionBody(e.getMessage());
    }
}