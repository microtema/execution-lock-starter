package de.microtema.execution.lock.service;


import de.microtema.execution.lock.repository.ExecutionLockJdbcTemplate;
import de.microtema.model.builder.annotation.Model;
import de.microtema.model.builder.util.FieldInjectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExecutionLockServiceTest {

    @InjectMocks
    ExecutionLockService sut;

    @Mock
    ExecutionLockJdbcTemplate jdbcTemplate;

    @Model
    String name;

    @Model
    Integer lockAtLeastFor;

    @Model
    Integer lockAtMostFor;

    @BeforeEach
    void setUp() {
        FieldInjectionUtil.injectFields(this);

        lockAtLeastFor = Math.abs(lockAtLeastFor);
        lockAtMostFor = Math.abs(lockAtMostFor);
    }

    @Test()
    void lockWillReturnFalse() {

        boolean answer = sut.lock(name, lockAtLeastFor.longValue(), lockAtMostFor.longValue());

        assertFalse(answer);
    }

    @Test()
    void lockWillReturnTrue() {

        when(jdbcTemplate.update(anyString(), any(Object[].class), any(int[].class))).thenReturn(1);

        boolean answer = sut.lock(name, lockAtLeastFor.longValue(), lockAtMostFor.longValue());

        assertTrue(answer);
    }

    @Test
    void unlockWillReturnFalse() {

        boolean answer = sut.unlock(name);

        assertFalse(answer);
    }

    @Test
    void unlockWillReturnTrue() {

        when(jdbcTemplate.update(anyString(), anyString(), anyString())).thenReturn(1);

        boolean answer = sut.unlock(name);

        assertTrue(answer);
    }

}
