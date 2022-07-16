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
    private Long id;

    @NotNull(message = "name can't be empty")
    @NotBlank(message = "name can't be empty")
    private String name;

    private String description;
    private LocalDate releaseDate;
    @Positive(message = "duration should be positive")
    private Long duration;
    private MPA mpa = new MPA();
    private Set<Genre> genres = new HashSet<>();
    public Film(Long id, String name, String description, LocalDate releaseDate, Long duration, Long mpaId, String mpaName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        mpa.setId(mpaId);
        mpa.setName(mpaName);
    }

    public Film( String name, String description, LocalDate releaseDate, Long duration, Long mpaId) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        mpa.setId(mpaId);
    }

    public Film(Long id, String name, String description, LocalDate releaseDate, Long duration, Long mpaId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        mpa.setId(mpaId);
    }
}
