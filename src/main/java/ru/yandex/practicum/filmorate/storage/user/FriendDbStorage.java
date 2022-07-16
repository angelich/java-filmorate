package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static ru.yandex.practicum.filmorate.model.FriendshipStatus.APPROVED;

/**
 * Дао друзей в БД
 */
@Repository
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sql = "INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID, STATUS) VALUES (?,?,?)";
        jdbcTemplate.update(sql, userId, friendId, APPROVED.getStatusCode());
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String sql = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getUserFriends(Long userId) {
        String sql = "SELECT * FROM USERS WHERE USER_ID IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE FRIENDSHIP.USER_ID = ?)";
        return jdbcTemplate.query(sql, UserDbStorage::makeUser, userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        String sql = "SELECT * FROM USERS WHERE USER_ID IN (" +
                "SELECT FRIEND_ID FROM FRIENDSHIP WHERE FRIENDSHIP.USER_ID = ?" +
                "INTERSECT " +
                "SELECT FRIEND_ID FROM FRIENDSHIP WHERE FRIENDSHIP.USER_ID = ?)";
        return jdbcTemplate.query(sql, UserDbStorage::makeUser, userId, otherId);
    }
}
