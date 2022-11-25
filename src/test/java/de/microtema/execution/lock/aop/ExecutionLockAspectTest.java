package de.microtema.execution.lock.aop;

import de.microtema.model.builder.annotation.Inject;
import de.microtema.model.builder.annotation.Model;
import de.microtema.model.builder.util.FieldInjectionUtil;
import org.aspectj.lang.annotation.RequiredTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExecutionLockAspectTest {

    @InjectMocks
    ExecutionLockAspect sut;

    @Model
    String id;

    @Model
    Map<String, Object> context;

    @Model
    Long count;

    @BeforeEach
    void setUp() {
        FieldInjectionUtil.injectFields(this);
    }

    @Test
    void compileExpression() {

        context.put("ID", id);

        String answer = sut.compileExpression("{{ID}}", context);

        assertNotNull(answer);

        assertEquals(id, answer);
    }

    @Test
    void parseLong() {

        Long answer = sut.parseLong(count.toString(), null);

        assertNotNull(answer);

        assertEquals(count, answer);
    }

    @Test
    void parseLongOnNull() {

        Long answer = sut.parseLong(null, count);

        assertNotNull(answer);

        assertEquals(count, answer);
    }

    @Test
    void parseLongOnNonNumeric() {

        Long answer = sut.parseLong(id, count);

        assertNotNull(answer);

        assertEquals(count, answer);
    }
}
