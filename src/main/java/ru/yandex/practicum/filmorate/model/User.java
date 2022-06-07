package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    Long id;
    @Email(message = "invalid email")
    @NotNull(message = "email can't be empty")
    @NotBlank(message = "email can't be empty")
    String email;
    @NotBlank(message = "login can't be empty")
    @NotNull(message = "login can't be empty")
    String login;
    String name;
    @Past(message = "birthday can't be in future")
    LocalDate birthday;
}