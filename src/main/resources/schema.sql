CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id   long PRIMARY KEY AUTO_INCREMENT,
    mpa_name varchar(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
    film_id      long PRIMARY KEY AUTO_INCREMENT,
    film_name    varchar(255) NOT NULL,
    description  varchar(255),
    release_date date,
    duration     long,
    film_mpa     long REFERENCES mpa (mpa_id)
);

CREATE TABLE IF NOT EXISTS users
(
    user_id   long PRIMARY KEY AUTO_INCREMENT,
    email     varchar(255) NOT NULL,
    login     varchar(255) NOT NULL,
    user_name varchar(255) NOT NULL,
    birthday  date
);

CREATE TABLE IF NOT EXISTS likes
(
    user_id long REFERENCES users (user_id),
    film_id integer REFERENCES films (film_id)
);

CREATE TABLE IF NOT EXISTS friendship
(
    user_id   long REFERENCES users (user_id),
    friend_id integer REFERENCES users (user_id),
    status    integer NOT NULL
);

CREATE TABLE IF NOT EXISTS genre
(
    genre_id   long PRIMARY KEY AUTO_INCREMENT,
    genre_name varchar(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  integer REFERENCES films (film_id),
    genre_id long REFERENCES genre (genre_id)
);

INSERT INTO MPA (MPA_NAME)
VALUES ('G');
INSERT INTO MPA (MPA_NAME)
VALUES ('PG');
INSERT INTO MPA (MPA_NAME)
VALUES ('NC-17');
INSERT INTO MPA (MPA_NAME)
VALUES ('R');
INSERT INTO MPA (MPA_NAME)
VALUES ('PG-13');

INSERT INTO GENRE (GENRE_NAME)
VALUES ( 'Comedy' );