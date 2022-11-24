package de.microtema.execution.lock.util;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;


public final class LockUtil {

    private final static MustacheFactory mustacheFactory = new DefaultMustacheFactory();

    public static String compileExpression(String template, Map<String, Object> context) {

        Mustache mustache = mustacheFactory.compile(new StringReader(template), template);

        Writer execute = mustache.execute(new StringWriter(), context);

        return execute.toString();
    }

    public static Long parseLong(String longValueStr, Long defaultValue) {
        try {
            return Long.valueOf(longValueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
