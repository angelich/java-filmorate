package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

/**
 * Сервис по работе с пользователями
 */
@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
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
        userExistOrThrow(user.getId());
        return userStorage.update(user);
    }

    public void addFriend(Long userId, Long friendId) {
        userExistOrThrow(userId);
        userExistOrThrow(friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        userExistOrThrow(userId);
        userExistOrThrow(friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getUserFriends(Long userId) {
        userExistOrThrow(userId);
        return friendStorage.getUserFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        log.info("Запрос общих друзей у пользователей {} и {}", userId, otherId);
        userExistOrThrow(userId);
        userExistOrThrow(otherId);
        return friendStorage.getCommonFriends(userId, otherId);
    }

    public User getUser(Long userId) {
        userExistOrThrow(userId);
        return userStorage.getUserOrThrow(userId);
    }

    private void userExistOrThrow(Long userId) {
        userStorage.getUserOrThrow(userId);
    }
}
