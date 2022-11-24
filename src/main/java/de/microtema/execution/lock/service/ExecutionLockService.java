package de.microtema.execution.lock.service;


import de.microtema.execution.lock.repository.ExecutionLockEntity;
import de.microtema.execution.lock.repository.ExecutionLockJdbcTemplate;
import de.microtema.execution.lock.repository.ExecutionLockWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@ConditionalOnProperty(prefix = "execution-lock", name = "disabled", havingValue = "false")
public class ExecutionLockService {

    private static final String APP_ID = Optional.ofNullable(System.getenv("HOSTNAME")).orElseGet(() -> UUID.randomUUID().toString());
    private static final int SUCCESS = 1;
    private static final int[] TYPES = new int[]{Types.VARCHAR, Types.TIMESTAMP, Types.TIMESTAMP, Types.VARCHAR};

    private final ExecutionLockJdbcTemplate jdbcTemplate;

    @Value("${spring.application.name}")
    private String applicationName;

    public ExecutionLockService(ExecutionLockJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean lock(String name, Long lockAtLeastFor, Long lockAtMostFor) {

        cleanup(name);

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime lockAtLeastUntil = now.plusSeconds(lockAtLeastFor);
        LocalDateTime lockAtMostUntil = now.plusSeconds(lockAtMostFor);

        String applicationId = applicationId();

        String tableName = jdbcTemplate.getTableName();

        Object[] params = new Object[]{name, lockAtLeastUntil, lockAtMostUntil, applicationId};

        int result = -1;

        try {
            result = jdbcTemplate.update("INSERT INTO " + tableName + " (NAME, LOCKED_AT, LOCK_UNTIL, LOCKED_BY) VALUES(?, ?, ?, ?)", params, TYPES);
        } catch (Exception e) {
            return false;
        }

        return result == SUCCESS;
    }

    public boolean unlock(String name) {

        String applicationId = applicationId();

        String tableName = jdbcTemplate.getTableName();

        int result = jdbcTemplate.update("DELETE " + tableName + " WHERE NAME =? AND LOCKED_BY =?", name, applicationId);

        return result == SUCCESS;
    }

    private void cleanup(String name) {

        String tableName = jdbcTemplate.getTableName();

        List<ExecutionLockEntity> executionLockList = jdbcTemplate.query("SELECT * FROM " + tableName + " WHERE NAME =?", new ExecutionLockWrapper(), name);

        if (executionLockList.isEmpty()) {
            return;
        }

        ExecutionLockEntity executionLock = executionLockList.get(0);

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime lockedDate = executionLock.getLockedDate();
        LocalDateTime lockUntil = executionLock.getLockUntil();

        String lockedBy = executionLock.getLockedBy();

        String applicationId = applicationId();

        boolean lockedByThisApplication = StringUtils.equals(lockedBy, applicationId);

        // validate lockedDate only
        if (lockedByThisApplication && now.isBefore(lockedDate)) {
            return;
        }

        // validate lockUntil now
        if (now.isBefore(lockUntil)) {
            return;
        }

        bruteForceUnlock(name);
    }

    private void bruteForceUnlock(String name) {

       // log.warn("BRUTE FORCE DELETE!! Lock: {}", name);

        String tableName = jdbcTemplate.getTableName();

        int result = jdbcTemplate.update("DELETE " + tableName + " WHERE NAME =?", name);

        Validate.isTrue(result == 1, "Unable to delete brute force lock %s!", tableName, name);
    }

    private String applicationId() {

        return applicationName + "#" + APP_ID;
    }
}
