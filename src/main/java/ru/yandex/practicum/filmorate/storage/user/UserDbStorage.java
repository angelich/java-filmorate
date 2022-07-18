package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Objects.requireNonNull;

/**
 * Хранилище пользователей в БД
 */
@Repository
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getAll() {
        final String sql = "SELECT USER_ID, EMAIL, LOGIN,USER_NAME, BIRTHDAY FROM USERS";
        return jdbcTemplate.query(sql, UserDbStorage::makeUser);
    }

    @Override
    public User getUserOrThrow(Long userId) {
        final String sql = "SELECT USER_ID, EMAIL, LOGIN,USER_NAME, BIRTHDAY FROM USERS WHERE USER_ID = ?";

        List<User> users = jdbcTemplate.query(sql, UserDbStorage::makeUser, userId);
        if (users.size() != 1) {
            throw new NoSuchElementException("Пользователя с таким идентификатором не существует");
        }
        return users.get(0);
    }

    public static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login( rs.getString("LOGIN"))
                .name( rs.getString("USER_NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .build();
    }

    @Override
    public User create(User user) {
        final String sql = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, new String[]{"USER_ID"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());
            LocalDate birthday = user.getBirthday();
            if (null == birthday) {
                statement.setNull(4, Types.DATE);
            } else {
                statement.setDate(4, Date.valueOf(birthday));
            }
            return statement;
        }, keyHolder);
        user.setId(requireNonNull(keyHolder.getKey()).longValue());

        log.info("Создан пользователь: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?,BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        log.info("Обновлен пользователь: {}", user);
        return user;
    }
}
