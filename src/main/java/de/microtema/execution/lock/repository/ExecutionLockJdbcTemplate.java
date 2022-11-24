package de.microtema.execution.lock.repository;


import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class ExecutionLockJdbcTemplate extends JdbcTemplate {

    private final String tableName;

    public ExecutionLockJdbcTemplate(DataSource dataSource, String tableName) {
        super(dataSource);
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
