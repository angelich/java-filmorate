package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;

public interface UserStorage {
    /**
     * @return всех пользователей
     */
    Collection<User> getAll();

    User getUser(Long userId);

    /**
     * @param user Объект пользователя
     * @return созданный пользователь
     */
    User create(@Valid @RequestBody User user);

    /**
     * @param user Обновленный объект пользователя
     * @return обновленный пользователь
     */
    User update(@Valid @RequestBody User user);
}
