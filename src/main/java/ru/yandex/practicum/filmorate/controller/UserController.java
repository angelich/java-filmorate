package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private static Long counter = 0L;

    public Long generateId() {
        return ++counter;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Все пользователи: {}", users.values());
        return users.values();
    }

    @PostMapping
    public User addUser( @Valid @RequestBody User user) {
        log.debug("Запрос на добавление пользователя: {}", user);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        Long userId = generateId();
        user.setId(userId);
        users.put(userId, user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Запрос на обновление пользователя: {}", user);
        if (user.getId() < 1) {
            throw new ValidationException("Неверный идентификатор пользователя");
        }
        users.put(user.getId(), user);
        log.info("Обновлен пользователь: {}", user);
        return user;
    }
}
