package de.microtema.execution.lock.repository;

import de.microtema.model.builder.annotation.Model;
import de.microtema.model.builder.util.FieldInjectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class ExecutionLockWrapperTest {

    @InjectMocks
    ExecutionLockWrapper sut;

    @Mock
    ResultSet rs;

    @Model
    int rowNum;

    @BeforeEach
    void setUp() {
        FieldInjectionUtil.injectFields(this);
    }

    @Test
    void mapRow() throws SQLException {

        Mockito.when(rs.getDate(eq("LOCKED_AT"))).thenReturn(Date.valueOf(LocalDate.now()));
        Mockito.when(rs.getDate(eq("LOCK_UNTIL"))).thenReturn(Date.valueOf(LocalDate.now()));

        ExecutionLockEntity answer = sut.mapRow(rs, rowNum);

        assertNotNull(answer);
    }
}
