package de.microtema.execution.lock.aop;


import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import de.microtema.execution.lock.config.ExecutionLockProperties;
import de.microtema.execution.lock.enums.ExecutionLock;
import de.microtema.execution.lock.service.ExecutionLockService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;


@Aspect
@Component
public class ExecutionLockAspect {

    private final static MustacheFactory mustacheFactory = new DefaultMustacheFactory();

    private final ExecutionLockService executionLockService;
    private final ExecutionLockProperties executionLockProperties;

    public ExecutionLockAspect(ExecutionLockService executionLockService, ExecutionLockProperties executionLockProperties) {
        this.executionLockService = executionLockService;
        this.executionLockProperties = executionLockProperties;
    }

    @Around("@annotation(de.microtema.execution.lock.enums.ExecutionLock)")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

        Signature signature = joinPoint.getStaticPart().getSignature();

        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] methodArguments = joinPoint.getArgs();
        Method method = methodSignature.getMethod();

        ExecutionLock executionLock = method.getAnnotation(ExecutionLock.class);

        Map<String, Object> payload = getPayload(parameterNames, methodArguments);

        String name = compileExpression(executionLock.name(), payload);
        Long lockAtLeastFor = getLongValueOrDefault(payload, executionLock.lockAtLeastFor(), executionLockProperties.getLockAtLeastFor());
        Long lockAtMostFor = getLongValueOrDefault(payload, executionLock.lockAtLeastFor(), executionLockProperties.getLockAtMostFor());

        validate(name, lockAtLeastFor, lockAtMostFor);

        if (!executionLockService.lock(name, lockAtLeastFor, lockAtMostFor)) {

           System.out.println("RACE CONDITION DETECTED!! Lock: " + name);

            return null;
        }

        try {
            return joinPoint.proceed();
        } finally {
            executionLockService.unlock(name);
        }

    }

    private Long getLongValueOrDefault(Map<String, Object> payload, String lockAtLeastForTemplate, Long defaultValue) {

        String longValueStr = compileExpression(lockAtLeastForTemplate, payload);

        Long longValue = parseLong(longValueStr, defaultValue);

        if (longValue == -1) {
            return defaultValue;
        }

        return longValue;
    }

    private void validate(String name, Long lockAtLeastFor, Long lockAtMostFor) {

        if (lockAtLeastFor.compareTo(lockAtMostFor) > 0) {
            throw new IllegalArgumentException("lockAtLeastFor is longer than lockAtMostFor for lock '" + name + "'.");
        }

        if (lockAtMostFor < 0) {
            throw new IllegalArgumentException("lockAtMostFor is negative '" + name + "'.");
        }

        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("lock name can not be empty");
        }
    }

    private Map<String, Object> getPayload(String[] parameterNames, Object[] methodArguments) {

        Map<String, Object> payload = new LinkedHashMap<>();

        for (int index = 0; index < parameterNames.length; index++) {

            String param = parameterNames[index];
            Object methodArgument = methodArguments[index];
            payload.put(param, methodArgument);
        }

        return payload;
    }

    protected String compileExpression(String template, Map<String, Object> context) {

        Mustache mustache = mustacheFactory.compile(new StringReader(template), template);

        Writer execute = mustache.execute(new StringWriter(), context);

        return execute.toString();
    }

    protected Long parseLong(String longValueStr, Long defaultValue) {
        try {
            return Long.valueOf(longValueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
