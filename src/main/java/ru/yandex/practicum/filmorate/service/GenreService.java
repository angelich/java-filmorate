package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Сервис по работе с жанрами фильмов
 */
@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Genre getGenre(Long genreId) {
        return genreStorage.getGenreOrThrow(genreId);
    }

    public Set<Genre> getFilmGenres(Long filmId) {
        return new LinkedHashSet<>(genreStorage.getFilmGenres(filmId));
    }
}
