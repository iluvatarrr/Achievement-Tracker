package ru.dmitriy.apprunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "ru.dmitriy.apprunner",
        "ru.dmitriy.userservice",
        "ru.dmitriy.goalservice",
        "ru.dmitriy.commondomain"
})
public class AppRunnerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppRunnerApplication.class, args);
    }
}