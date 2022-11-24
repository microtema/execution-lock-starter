package de.microtema.execution.lock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@ConfigurationProperties(prefix = "execution-lock")
public class ExecutionLockProperties {

    private String lockTableName = "SHEDLOCK";

    private Long lockAtLeastFor = TimeUnit.SECONDS.toSeconds(90);

    private Long lockAtMostFor = TimeUnit.MINUTES.toSeconds(3);

    public String getLockTableName() {
        return lockTableName;
    }

    public void setLockTableName(String lockTableName) {
        this.lockTableName = lockTableName;
    }

    public Long getLockAtLeastFor() {
        return lockAtLeastFor;
    }

    public void setLockAtLeastFor(Long lockAtLeastFor) {
        this.lockAtLeastFor = lockAtLeastFor;
    }

    public Long getLockAtMostFor() {
        return lockAtMostFor;
    }

    public void setLockAtMostFor(Long lockAtMostFor) {
        this.lockAtMostFor = lockAtMostFor;
    }
}
