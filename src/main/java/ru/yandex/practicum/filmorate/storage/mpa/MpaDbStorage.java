package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> getAllMpa() {
        String sql = "SELECT MPA_ID, MPA_NAME FROM MPA";
       return jdbcTemplate.query(sql, MpaDbStorage::makeMpa);
    }

    @Override
    public MPA getMpaOrThrow(Long mpaId) {
        String sql = "SELECT MPA_ID, MPA_NAME FROM MPA WHERE MPA_ID = ?";
        List<MPA> mpaList = jdbcTemplate.query(sql, MpaDbStorage::makeMpa, mpaId);
        if (mpaList.size() != 1) {
            throw new NoSuchElementException("МПА с таким идентификатором не существует");
        }
        return mpaList.get(0);
    }

    static MPA makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new MPA(rs.getLong("MPA_ID"), rs.getString("MPA_NAME"));
    }
}
