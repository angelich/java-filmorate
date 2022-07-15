package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

/**
 * Сервис по работе с фильмами
 */
@Service
@Slf4j
public class FilmService {
    public static final int DESCRIPTION_MAX_LENGTH = 200;
    public static final LocalDate FILMOGRAPHY_START_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final GenreService genreService;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, GenreService genreService) {
        this.filmStorage = filmStorage;
        this.genreService = genreService;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        validateFilm(film);
        return filmStorage.create(film);
    }

    public Film update(@Valid @RequestBody Film film) {
        filmExistOrThrow(film.getId());
        validateFilm(film);
        var newFilm = filmStorage.update(film);
        newFilm.setGenres(genreService.getFilmGenres(film.getId()));
        return newFilm;
    }

    public Film getFilm(Long filmId) {
        var film = filmStorage.getFilmOrThrow(filmId);
        film.setGenres(genreService.getFilmGenres(filmId));
        return film;
    }

    private void filmExistOrThrow(Long filmId) {
        filmStorage.getFilmOrThrow(filmId);
    }

    private static void validateFilm(Film film) {
        try {
            if (film.getDescription().length() > DESCRIPTION_MAX_LENGTH) {
                throw new ValidationException("Максимальная длина описания — 200 символов");
            }
            if (film.getReleaseDate().isBefore(FILMOGRAPHY_START_DATE)) {
                throw new ValidationException("Дата релиза не может быть раньше даты рождения кино");
            }
        } catch (ValidationException e) {
            log.error(e.getMessage());
            throw new ValidationException(e.getMessage());
        }
    }
}
