package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Дао фильмов в БД
 */
@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getAll() {
        String sql = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILM_MPA, MPA_NAME FROM FILMS AS F" +
                " JOIN MPA AS M ON F.FILM_MPA = M.MPA_ID";
        return jdbcTemplate.query(sql, FilmDbStorage::makeFilm);
    }

    @Override
    public Film getFilmOrThrow(Long filmId) {
        final String sql = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILM_MPA, MPA_NAME FROM FILMS AS F" +
                " JOIN MPA AS M ON F.FILM_MPA = M.MPA_ID" +
                " WHERE FILM_ID = ?";

        List<Film> films = jdbcTemplate.query(sql, FilmDbStorage::makeFilm, filmId);
        if (films.size() != 1) {
            throw new NoSuchElementException("Фильм не найден");
        }
        return films.get(0);
    }

    public static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("FILM_ID"))
                .name(rs.getString("FILM_NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getLong("DURATION"))
                .mpa(new MPA(rs.getLong("FILM_MPA"), rs.getString("MPA_NAME")))
                .build();
    }

    @Override
    public Film create(Film film) {
        final String sql = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILM_MPA) VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, new String[]{"film_id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setLong(4, film.getDuration());
            statement.setLong(5, film.getMpa().getId());
            return statement;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());

        for (Genre genre : film.getGenres()) {
            String sqlQuery = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?,?)";
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }

        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film update(@Valid @RequestBody Film film) {
        String sql = "UPDATE FILMS SET FILM_NAME= ?, DESCRIPTION= ?, RELEASE_DATE= ?, DURATION= ?, FILM_MPA = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        String sqlDelete = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlDelete, film.getId());

        for (Genre genre : film.getGenres()) {
            String sqlInsert = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?,?)";
            jdbcTemplate.update(sqlInsert, film.getId(), genre.getId());
        }

        log.info("Обновлен фильм: {}", film);
        return film;
    }

    public List<Film> getPopularFilmList(Long count) {
        String sql = "SELECT * FROM FILMS AS F " +
                "LEFT JOIN MPA M on F.FILM_MPA = M.MPA_ID " +
                "LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID " +
                "GROUP BY F.FILM_ID ORDER BY COUNT(L.FILM_ID) DESC LIMIT ?";

        return jdbcTemplate.query(sql, FilmDbStorage::makeFilm, count);
    }
}
