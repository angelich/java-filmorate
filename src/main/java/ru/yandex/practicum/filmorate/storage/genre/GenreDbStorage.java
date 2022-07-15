package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT GENRE_ID, GENRE_NAME FROM GENRE";
        return jdbcTemplate.query(sql, GenreDbStorage::makeGenre);
    }


    @Override
    public Genre getGenreOrThrow(Long genreId) {
        String sql = "SELECT GENRE_ID, GENRE_NAME FROM GENRE WHERE GENRE_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sql, GenreDbStorage::makeGenre, genreId);
        if (genres.size() != 1) {
            throw new NoSuchElementException("Жанра с таким идентификатором не существует");
        }
        return genres.get(0);
    }

    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getLong("GENRE_ID"), rs.getString("GENRE_NAME"));
    }

    public List<Genre> getFilmGenres(Long filmId) {
        String sql = "SELECT DISTINCT * FROM FILM_GENRE F JOIN GENRE G2 on G2.GENRE_ID = F.GENRE_ID WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, GenreDbStorage::makeGenre, filmId);
    }
}
