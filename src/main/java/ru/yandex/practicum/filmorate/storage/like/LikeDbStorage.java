package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Дао лайков в БД
 */
@Repository
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(Long filmId, Long userId) {
        String sql = "INSERT INTO LIKES (USER_ID, FILM_ID) VALUES ( ?, ? )";
        jdbcTemplate.update(sql, userId, filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        String sql = "DELETE FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?";
        jdbcTemplate.update(sql, userId, filmId);
    }
}
