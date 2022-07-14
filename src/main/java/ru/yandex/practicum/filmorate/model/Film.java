package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Film {
    public Film(Long id, String name, String description, LocalDate releaseDate, Long duration, MPA mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    private Long id;

    @NotNull(message = "name can't be empty")
    @NotBlank(message = "name can't be empty")
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "duration should be positive")
    private Long duration;
    private Set<Long> likes = new HashSet<>();
    private MPA mpa;
    private Set<Genre> genres = new HashSet<>();

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);
    }
}
