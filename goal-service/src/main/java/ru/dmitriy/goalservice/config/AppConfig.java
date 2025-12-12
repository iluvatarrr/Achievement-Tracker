package ru.dmitriy.goalservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.dmitriy.goalservice.web.mapper.Mappable;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    public Map<String, Mappable> mappableMap() {
        return new HashMap<>();
    }
}
