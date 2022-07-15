package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

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
        String sql = "SELECT * FROM FILMS AS F " +
                "LEFT JOIN MPA M on F.FILM_MPA = M.MPA_ID " +
                "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID " +
                "GROUP BY F.FILM_ID ORDER BY COUNT(L.FILM_ID) DESC LIMIT ?";

        return jdbcTemplate.query(sql, FilmDbStorage::makeFilm, count);
    }
}
