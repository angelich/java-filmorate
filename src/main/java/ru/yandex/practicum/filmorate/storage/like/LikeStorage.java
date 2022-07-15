package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {

    /**
     * Добавляет лайк фильму от юзера
     */
    void addLike(Long filmId, Long userId);

    /**
     * Удаляет лайк
     */
    void removeLike(Long filmId, Long userId);

    /**
     * @param count максимальное количество фильмов, отсортированных по количеству лайков
     * @return список фильмов
     */
    List<Film> getPopularFilmList(Long count);
}
