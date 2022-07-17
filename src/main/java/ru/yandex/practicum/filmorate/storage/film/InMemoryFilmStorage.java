package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Хранилище фильмов в памяти
 */
@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private Long counter = 0L;

    public Long generateId() {
        return ++counter;
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film getFilmOrThrow(Long filmId) {
        return films.get(filmId);
    }

    @Override
    public Film create( Film film) {
        Long filmId = generateId();
        film.setId(filmId);
        films.put(filmId, film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film update(@Valid @RequestBody Film film) {
        films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }
@Override
    public List<Film> getPopularFilmList(Long count) {
        return films.values().stream()
                .sorted((o1, o2) ->  o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
