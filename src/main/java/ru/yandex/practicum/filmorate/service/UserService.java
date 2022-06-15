package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        log.debug("Запрос на добавление пользователя: {}", user);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.create(user);
    }

    public User update(User user) {
        log.debug("Запрос на обновление пользователя: {}", user);
        if (user.getId() < 1) {
            throw new ValidationException("Неверный идентификатор пользователя");
        }
        return userStorage.update(user);
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.getUser(userId).addFriend(friendId);
        userStorage.getUser(friendId).addFriend(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        userStorage.getUser(userId).deleteFriend(friendId);
        userStorage.getUser(friendId).deleteFriend(userId);
    }

    public List<User> getUserFriends(Long userId) {
        return userStorage.getAll().stream()
                .filter(user -> userStorage.getUser(userId).getFriends().contains(user.getId()))
                .collect(Collectors.toList());
    }
}
