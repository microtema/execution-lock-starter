package de.microtema.execution.lock.repository;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ExecutionLockWrapper implements RowMapper<ExecutionLockEntity> {

    @Override
    public ExecutionLockEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

        ExecutionLockEntity lockEntity = new ExecutionLockEntity();

        lockEntity.setName(rs.getString("NAME"));
        lockEntity.setLockedBy(rs.getString("LOCKED_BY"));

        lockEntity.setLockedDate(convertToLocalDateTimeViaInstant(rs.getDate("LOCKED_AT")));
        lockEntity.setLockUntil(convertToLocalDateTimeViaInstant(rs.getDate("LOCK_UNTIL")));

        return lockEntity;
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {

        return Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
