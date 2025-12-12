package ru.dmitriy.userservice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.event.UserValidationResponseEvent;
import ru.dmitriy.commondomain.event.ValidateUserEvent;
import ru.dmitriy.userservice.repository.UserRepository;

@Service
public class UserValidationListener {

    private static final Logger log = LoggerFactory.getLogger(UserValidationListener.class);
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UserValidationListener(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Слушаем события запросов на валидацию пользователя
     */
    @EventListener
    @Async
    public void handleValidateUserEvent(ValidateUserEvent event) {
        log.debug("Получен запрос на валидацию пользователя: {}", event.userId());
        try {
            boolean exists = userRepository.existsById(event.userId());
            var message = exists ? "Пользователь найден" : "Пользователь не найден";
            UserValidationResponseEvent response = new UserValidationResponseEvent(event.userId(),exists, event.requestId(),message);
            eventPublisher.publishEvent(response);
            log.debug("Отправлен ответ на валидацию пользователя: {}", event.userId());
        } catch (Exception e) {
            log.error("Ошибка при проверке пользователя: {}", event.userId(), e);
            UserValidationResponseEvent response = new UserValidationResponseEvent(event.userId(),false, event.requestId(),"Ошибка при проверке пользователя");
            eventPublisher.publishEvent(response);
        }
    }
}