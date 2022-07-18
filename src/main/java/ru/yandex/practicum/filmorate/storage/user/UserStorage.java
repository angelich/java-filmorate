package ru.yandex.practicum.filmorate.storage.user;

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
    User getUserOrThrow(Long userId);

    /**
     * Создание пользователя
     */
    User create(@Valid @RequestBody User user);

    /**
     * Обновление пользователя
     */
    User update(@Valid @RequestBody User user);
}
