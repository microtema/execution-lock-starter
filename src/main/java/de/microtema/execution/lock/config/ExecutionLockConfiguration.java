package de.microtema.execution.lock.config;

import de.microtema.execution.lock.aop.ExecutionLockAspect;
import de.microtema.execution.lock.service.ExecutionLockService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
        ExecutionLockAspect.class,
        ExecutionLockService.class,
        ExecutionLockProperties.class
})
public class ExecutionLockConfiguration {

}
