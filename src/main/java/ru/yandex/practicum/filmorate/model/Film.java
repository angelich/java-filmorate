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
    Long id;
    @NotNull(message = "name can't be empty")
    @NotBlank(message = "name can't be empty")
    String name;
    String description;
    LocalDate releaseDate;
    @Positive(message = "duration should be positive")
    Long duration;
    Set<Long> likes = new HashSet<>();
    Long mpa;
    Set<Genre> genres;

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);
    }
}
