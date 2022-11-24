package de.microtema.execution.lock.enums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecutionLock {

    /**
     * Lock name. may be a mustache expression
     */
    String name() default "";

    /**
     * How long (in ms) the lock should be kept in case the machine which obtained the lock died before releasing it.
     * This is just a fallback, under normal circumstances the lock is released as soon the tasks finishes.
     * <p>
     * Negative value means default
     */
    String lockAtMostFor() default "-1";

    /**
     * The lock will be held at least for X millis. Can be used if you really need to execute the task
     * at most once in given period of time. If the duration of the task is shorter than clock difference between nodes, the task can
     * be theoretically executed more than once (one node after another). By setting this parameter, you can make sure that the
     * lock will be kept at least for given period of time.
     * <p>
     * Negative value means default
     */
    String lockAtLeastFor() default "-1";

}
