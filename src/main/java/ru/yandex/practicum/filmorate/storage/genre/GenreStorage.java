package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    /**
     * @return Список всех жанров
     */
    List<Genre> getAllGenres();

    /**
     * @param genreId идентификатор жанра
     * @return жанр по идентификатору, либо выбрасывает ошибку при отсутствии
     */
    Genre getGenreOrThrow(Long genreId);

    /**
     * @param filmId идентификатор фильма
     * @return жанр фильма
     */
    List<Genre> getFilmGenres(Long filmId);
}
