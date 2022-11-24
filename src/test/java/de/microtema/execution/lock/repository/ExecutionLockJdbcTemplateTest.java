package de.microtema.execution.lock.repository;

import de.microtema.model.builder.annotation.Model;
import de.microtema.model.builder.util.FieldInjectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ExecutionLockJdbcTemplateTest {

    @InjectMocks
    ExecutionLockJdbcTemplate sut;

    @Mock
    DataSource dataSource;

    @Model
    String tableName;

    @BeforeEach
    void setUp() {
        FieldInjectionUtil.injectFields(this);
    }

    @Test
    void create(){

        ExecutionLockJdbcTemplate answer = new ExecutionLockJdbcTemplate(dataSource, tableName);

        assertNotNull(answer);
        assertEquals(tableName, answer.getTableName());
    }
}
