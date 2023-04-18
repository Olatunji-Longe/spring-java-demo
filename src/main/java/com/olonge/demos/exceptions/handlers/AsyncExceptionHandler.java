package com.olonge.demos.exceptions.handlers;

import com.olonge.demos.exceptions.AsyncProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
        throw new AsyncProcessingException(throwable, method, params);
    }

}
