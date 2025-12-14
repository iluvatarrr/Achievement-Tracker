package ru.dmitriy.apprunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "ru.dmitriy")
@ComponentScan(basePackages = {
        "ru.dmitriy.apprunner",
        "ru.dmitriy.userservice",
        "ru.dmitriy.goalservice",
        "ru.dmitriy.groupservice",
        "ru.dmitriy.notificationservice",
        "ru.dmitriy.commondomain"
})
public class AppRunnerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppRunnerApplication.class, args);
    }
}