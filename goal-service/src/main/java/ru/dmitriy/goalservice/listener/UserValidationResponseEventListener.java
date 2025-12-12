package ru.dmitriy.goalservice.listener;

import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.event.UserValidationResponseEvent;
import ru.dmitriy.commondomain.event.ValidateUserEvent;
import ru.dmitriy.goalservice.service.impl.GoalServiceImpl;
import javax.naming.ServiceUnavailableException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class UserValidationResponseEventListener {
    private static final Logger log = LoggerFactory.getLogger(GoalServiceImpl.class);
    private final ApplicationEventPublisher eventPublisher;
    private final Map<String, CompletableFuture<Boolean>> pendingRequests = new ConcurrentHashMap<>();

    public UserValidationResponseEventListener(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public boolean validateUser(Long userId) throws ServiceUnavailableException {
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<Boolean> responseFuture = new CompletableFuture<>();
        pendingRequests.put(requestId, responseFuture);
        try {
            ValidateUserEvent event = new ValidateUserEvent(userId, requestId);
            log.debug("Отправка запроса на валидацию пользователя: {}", userId);
            eventPublisher.publishEvent(event);
            return responseFuture.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Таймаут при проверке пользователя: {}", userId);
            pendingRequests.remove(requestId);
            throw new ServiceUnavailableException("Сервис пользователей недоступен");
        } catch (InterruptedException | ExecutionException e) {
            log.error("Ошибка при проверке пользователя: {}", userId, e);
            pendingRequests.remove(requestId);
            throw new ValidationException("Ошибка валидации пользователя");
        }
    }

    @EventListener
    public void handleUserValidationResponse(UserValidationResponseEvent response) {
        log.debug("Получен ответ на валидацию пользователя: {}, exists: {}",
                response.userId(), response.exists());
        CompletableFuture<Boolean> future = pendingRequests.remove(response.requestId());
        if (future != null) {
            future.complete(response.exists());
        }
    }
}