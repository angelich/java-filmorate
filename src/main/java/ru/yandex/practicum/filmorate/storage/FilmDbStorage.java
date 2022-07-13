package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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

@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getAll() {
        String sql = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILM_MPA FROM FILMS";
        return jdbcTemplate.query(sql, FilmDbStorage::makeFilm);
    }

    @Override
    public Film getFilmOrThrow(Long filmId) {
        final String sql = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, FILM_MPA FROM FILMS" +
              //  "JOIN MPA ON FILM.FILM_MPA = MPA.MPA_ID" +
                " WHERE FILM_ID = ?";

        List<Film> films = jdbcTemplate.query(sql, FilmDbStorage::makeFilm, filmId);
        if (films.size() != 1) {
            throw new ValidationException("Фильм не найден");
        }
        return films.get(0);
    }

    static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getLong("DURATION"),
             //   rs.getObject("MPA_NAME", MPA.class),
                rs.getObject("FILM_MPA", MPA.class));
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

        log.info("Обновлен фильм: {}", film);
        return film;
    }
}
