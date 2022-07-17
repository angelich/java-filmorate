package ru.yandex.practicum.filmorate.storage.like;

public interface LikeStorage {

    /**
     * Добавляет лайк фильму от юзера
     */
    void addLike(Long filmId, Long userId);

    /**
     * Удаляет лайк
     */
    void removeLike(Long filmId, Long userId);
}
