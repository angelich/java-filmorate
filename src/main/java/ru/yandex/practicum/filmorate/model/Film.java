package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class Film {
    int id;
    @NotNull
    @NotBlank
    String name;
    String description;
    LocalDate releaseDate;
    @Positive
    Long duration;
}
