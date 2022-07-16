package ru.yandex.practicum.filmorate.model;

/**
 * Статус дружбы между пользователями
 */
public enum FriendshipStatus {
    /**
     * Неподтвержденная
     */
    UNAPPROVED(0),
    /**
     * Подтвержденная
     */
    APPROVED(1);

   private final int statusCode;

    FriendshipStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
