package com.olonge.demos.exceptions;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AsyncProcessingException extends RuntimeException {

    public AsyncProcessingException(Throwable throwable, Method method, Object... params) {
        super(buildMessage(method, params), throwable);
    }

    private static String buildMessage(Method method, Object... params) {
        return String.format(
                "Exception thrown at Method: %s, params: [%s]",
                method.getName(),
                Stream.of(params).map(Object::toString).collect(Collectors.joining(","))
        );
    }
}
