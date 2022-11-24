package de.microtema.execution.lock.util;

import de.microtema.model.builder.annotation.Model;
import de.microtema.model.builder.util.FieldInjectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.MockUtil;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LockUtilTest {

    LockUtil sut;

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

        String answer = LockUtil.compileExpression("{{ID}}", context);

        assertNotNull(answer);

        assertEquals(id, answer);
    }

    @Test
    void parseLong() {

        Long answer = LockUtil.parseLong(count.toString(), null);

        assertNotNull(answer);

        assertEquals(count, answer);
    }

    @Test
    void parseLongOnNull() {

        Long answer = LockUtil.parseLong(null, count);

        assertNotNull(answer);

        assertEquals(count, answer);
    }

    @Test
    void parseLongOnNonNumeric() {

        Long answer = LockUtil.parseLong(id, count);

        assertNotNull(answer);

        assertEquals(count, answer);
    }
}
