package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static java.time.LocalDate.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final FriendDbStorage friendStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;

    @Test
    void testFindUserById() {
        User newUser = User.builder()
                .email("email@ma.ru")
                .login("login")
                .name("name")
                .birthday(now().minusYears(20L))
                .build();

        User createdUser = userStorage.create(newUser);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserOrThrow(createdUser.getId()));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrProperty("id");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "email@ma.ru");
                            assertThat(user).hasFieldOrPropertyWithValue("login", "login");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "name");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday", now().minusYears(20L));
                        }
                );
    }

    @Test
    void testGetAllUser() {
        User newUser1 = User.builder()
                .email("email@ma.ru")
                .login("login 1")
                .name("name 1")
                .birthday(now().minusYears(20L))
                .build();

        User newUser2 = User.builder()
                .email("email2@ma.ru")
                .login("login 2")
                .name("name 2")
                .birthday(now().minusYears(30L))
                .build();

        userStorage.create(newUser1);
        userStorage.create(newUser2);

        Collection<User> users = userStorage.getAll();
        assertTrue(!users.isEmpty());
    }

    @Test
    void testUpdateUser() {
        User newUser = User.builder()
                .email("emailForUpdate@ma.ru")
                .login("loginForUpdate")
                .name("nameForUpdate")
                .birthday(now().minusYears(20L))
                .build();

        User createdUser = userStorage.create(newUser);

        User userForUpdate = User.builder()
                .id(createdUser.getId())
                .email("updateduser@email.ru")
                .login("updatedLogin")
                .name("updatedName")
                .birthday(now().minusYears(20L))
                .build();

        Optional<User> userOptional = Optional.ofNullable(userStorage.update(userForUpdate));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", createdUser.getId());
                            assertThat(user).hasFieldOrPropertyWithValue("email", "updateduser@email.ru");
                            assertThat(user).hasFieldOrPropertyWithValue("login", "updatedLogin");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "updatedName");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday", now().minusYears(20L));
                        }
                );
    }

    @Test
    void testFindFilmById() {
        Film newFilm = Film.builder()
                .name("filmName")
                .description("film description")
                .releaseDate(now().minusYears(20L))
                .duration(100L)
                .mpa(new MPA(1L, "G"))
                .genres(new HashSet<>())
                .build();
        Film createdFilm = filmStorage.create(newFilm);
        var filmOptional = Optional.ofNullable(filmStorage.getFilmOrThrow(createdFilm.getId()));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrProperty("id");
                            assertThat(film).hasFieldOrPropertyWithValue("name", "filmName");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "film description");
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate", now().minusYears(20L));
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 100L);
                        }
                );
    }

    @Test
    void testUpdateFilm() {
        Film newFilm = Film.builder()
                .name("filmNameForUpdate")
                .description("film description for update")
                .releaseDate(now().minusYears(20L))
                .duration(100L)
                .mpa(new MPA(1L, "G"))
                .genres(new HashSet<>())
                .build();

        Film createdFilm = filmStorage.create(newFilm);

        Film filmForUpdate = Film.builder()
                .id(createdFilm.getId())
                .name("updated name")
                .description("updated description")
                .releaseDate(now().minusYears(30L))
                .duration(200L)
                .mpa(new MPA(2L, "PG"))
                .genres(new HashSet<>())
                .build();

        var filmOptional = Optional.ofNullable(filmStorage.update(filmForUpdate));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", createdFilm.getId());
                            assertThat(film).hasFieldOrPropertyWithValue("name", "updated name");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "updated description");
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate", now().minusYears(30L));
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 200L);
                        }
                );
    }

    @Test
    void changeFriendshipTest() {
        var user = userStorage.create(
                User.builder()
                        .email("email@ma.ru")
                        .login("login user")
                        .name("name user")
                        .birthday(now().minusYears(20L))
                        .build());

        var friend = userStorage.create(
                User.builder()
                        .email("emailfriend@ma.ru")
                        .login("login friend")
                        .name("name friend")
                        .birthday(now().minusYears(20L))
                        .build());

        friendStorage.addFriend(user.getId(), friend.getId());
        var userFriends = friendStorage.getUserFriends(user.getId());
        assertEquals(friend.getId(), userFriends.get(0).getId(), "Идентификатор друга должен совпадать");

        friendStorage.deleteFriend(user.getId(), friend.getId());
        userFriends = friendStorage.getUserFriends(user.getId());
        assertTrue(userFriends.isEmpty(), "После удаления у пользователя не должно остаться друзей");
    }

    @Test
    void testFindGenreById() {
        Optional<Genre> genreOptional = Optional.ofNullable(genreStorage.getGenreOrThrow(1L));

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre -> {
                            assertThat(genre).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
                        }
                );
    }

    @Test
    void testFindMpaById() {
        Optional<MPA> mpaOptional = Optional.ofNullable(mpaStorage.getMpaOrThrow(1L));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> {
                            assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
                        }
                );
    }
}
