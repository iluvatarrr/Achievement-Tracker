package ru.dmitriy.apprunner.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {
        "ru.dmitriy.commondomain.domain.goal",
        "ru.dmitriy.commondomain.domain.user",
        "ru.dmitriy.commondomain.domain.group"
})
@EnableJpaRepositories(basePackages = {
        "ru.dmitriy.userservice.repository",
        "ru.dmitriy.goalservice.repository",
        "ru.dmitriy.groupservice.repository"
})
public class JpaConfig {
}
