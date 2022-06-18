package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    public static final int DESCRIPTION_MAX_LENGTH = 200;
    public static final long COUNT_OF_TOP_FILMS = 10L;
    public static final LocalDate FILMOGRAPHY_START_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film create(Film film) {
        validateFilm(film);
        return filmStorage.create(film);
    }

    public Film update(@Valid @RequestBody Film film) {
        validateFilm(film);
        if (film.getId() < 1) {
            throw new ValidationException("Неверный идентификатор фильма");
        }
        return filmStorage.update(film);
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

    public void addLike(Long filmId, Long userId) {
        filmStorage.getFilm(filmId).addLike(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        filmStorage.getFilm(filmId).removeLike(userId);
    }

    public List<Film> getPopularFilmList() {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt(value -> value.getLikes().size()))
                .limit(COUNT_OF_TOP_FILMS)
                .collect(Collectors.toList());
    }

    public Film getFilm(Long filmId) {
        return filmStorage.getFilm(filmId);
    }
}
