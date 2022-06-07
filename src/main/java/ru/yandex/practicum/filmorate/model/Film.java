package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class Film {
    Long id;
    @NotNull(message = "name can't be empty")
    @NotBlank(message = "name can't be empty")
    String name;
    String description;
    LocalDate releaseDate;
    @Positive(message = "duration should be positive")
    Long duration;
}
