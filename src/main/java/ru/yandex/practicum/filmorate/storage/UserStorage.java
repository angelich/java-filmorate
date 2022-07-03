package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;

public interface UserStorage {
    /**
     * Получение всех пользователей
     */
    Collection<User> getAll();

    /**
     * Получение пользователя
     */
    User getUser(Long userId);

    /**
     * Создание пользователя
     */
    User create(@Valid @RequestBody User user);

    /**
     * Обновление пользователя
     */
    User update(@Valid @RequestBody User user);
}
