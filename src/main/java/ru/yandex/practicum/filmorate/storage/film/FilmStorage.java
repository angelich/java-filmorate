package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    /**
     * Возвращает все фильмы
     * @return коллекцию фильмов
     */
    Collection<Film> getAll();

    Film getFilmOrThrow(Long filmId);

    /**
     * @param film Объект фильма
     * @return созданный фильм
     */
    Film create(Film film);

    /**
     * @param film Обновленный объект фильма
     * @return обновленный фильм
     */
    Film update(Film film);

    /**
     * @param count максимальное количество фильмов, отсортированных по количеству лайков
     * @return список фильмов
     */
    List<Film> getPopularFilmList(Long count);
}
