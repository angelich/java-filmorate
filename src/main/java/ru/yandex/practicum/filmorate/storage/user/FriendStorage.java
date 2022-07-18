package ru.yandex.practicum.filmorate.storage.user;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    /**
     * Добавляет пользователя в друзья
     */
    void addFriend(Long userId, Long friendId);

    /**
     * Удаляет пользователя из друзей
     */
    void deleteFriend(Long userId, Long friendId);

    /**
     * Возвращает список друзей пользователя
     */
    List<User> getUserFriends(Long userId);

    /**
     * Возвращает список общих друзей двух пользователей
     */
    List<User> getCommonFriends(Long userId, Long otherId);
}
