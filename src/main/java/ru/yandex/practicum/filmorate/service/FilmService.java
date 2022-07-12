package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Сервис по работе с фильмами
 */
@Service
@Slf4j
public class FilmService {
    public static final int DESCRIPTION_MAX_LENGTH = 200;
    public static final LocalDate FILMOGRAPHY_START_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        return filmStorage.update(film);
    }

    public void addLike(Long filmId, Long userId) {
        filmExistOrThrow(filmId);
        userExistOrThrow(userId);
        filmStorage.getFilm(filmId).addLike(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        filmExistOrThrow(filmId);
        userExistOrThrow(userId);
        filmStorage.getFilm(filmId).removeLike(userId);
    }

    public List<Film> getPopularFilmList(Long count) {
        return filmStorage.getAll().stream()
                .sorted((o1, o2) ->  o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilm(Long filmId) {
        filmExistOrThrow(filmId);
        return filmStorage.getFilm(filmId);
    }

    private void userExistOrThrow(Long userId) {
        boolean isUserNotExist = userStorage.getAll().stream()
                .noneMatch(user -> user.getId().equals(userId));
        if (isUserNotExist) {
            throw new NoSuchElementException("Пользователя с таким идентификатором не существует");
        }
    }

    private void filmExistOrThrow(Long filmId) {
        boolean isFilmNotExist = filmStorage.getAll().stream()
                .noneMatch(film -> film.getId().equals(filmId));
        if (isFilmNotExist) {
            throw new NoSuchElementException("Фильма с таким идентификатором не существует");
        }
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
