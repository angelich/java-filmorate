package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

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

    public List<Film> getPopularFilmList(Long count) {
        String sql = "SELECT * FROM FILMS WHERE FILM_ID IN (" +
                "SELECT FILM_ID FROM LIKES GROUP BY FILM_ID ORDER BY COUNT(LIKES.FILM_ID) DESC LIMIT ?)";
        return jdbcTemplate.query(sql, FilmDbStorage::makeFilm, count);
    }
}
