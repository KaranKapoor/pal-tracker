package io.pivotal.pal.tracker.repository;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.pivotal.pal.tracker.TimeEntry;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Generated;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcTimeEntryRepository implements ITimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    /*class TimeEntryRowMapper implements RowMapper<TimeEntry> {

        public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            TimeEntry entry = new TimeEntry(rs.getLong("id"),
                    rs.getLong("project_id"),
                    rs.getLong("user_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getInt("hours"));

            return entry;
        }
    }*/

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry entry) {

        GeneratedKeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement =
                        con.prepareStatement("insert into time_entries (project_id, user_id, date, hours) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setLong(1, entry.getProjectId());
                statement.setLong(2, entry.getUserId());
                statement.setDate(3, Date.valueOf(entry.getDate()));
                statement.setInt(4, entry.getHours());
                return statement;
            }
        }, holder);

        long id = holder.getKey().longValue();

        return find(id);
    }

    @Override
    public TimeEntry find(long timeEntryId) {

        try {
            //return jdbcTemplate.queryForObject("select * from time_entries where id = ?", new Object[]{timeEntryId}, new TimeEntryRowMapper());
            return (TimeEntry) jdbcTemplate.queryForObject("select * from time_entries where id = ?", new Object[]{timeEntryId}, new BeanPropertyRowMapper(TimeEntry.class));
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public TimeEntry update(long timeEntryid, TimeEntry entry) {

        Object[] data = new Object[] {entry.getProjectId(), entry.getUserId(), Date.valueOf(entry.getDate()), entry.getHours(), timeEntryid};
        int[] types = {Types.BIGINT, Types.BIGINT, Types.DATE, Types.INTEGER, Types.BIGINT};

        jdbcTemplate.update("update time_entries set project_id=?, user_id=?, date=?, hours=? where id=?", data, types);

        return find(timeEntryid);
    }

    @Override
    public TimeEntry delete(long timeEntryId) {

        Object[] data = new Object[] {timeEntryId};
        int[] types = {Types.BIGINT};

        jdbcTemplate.update("delete from time_entries where id=?", data, types);

        return find(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {

        List<TimeEntry> response = new ArrayList<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("select * from time_entries");

        for (Map row : rows) {
            TimeEntry entry = new TimeEntry((long) row.get("id"),
                    (long) row.get("project_id"),
                    (long) row.get("user_id"),
                    ((Date) row.get("date")).toLocalDate(),
                    (int) row.get("hours"));
            response.add(entry);
        }

        return response;
    }
}
