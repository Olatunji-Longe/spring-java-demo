package com.olonge.demos.configs;

import com.olonge.demos.annotations.Exitable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Configuration
@RequiredArgsConstructor
public class AspectConfig {

    private static final String LOG_PREFIX = "[EXITABLE]";

    @Pointcut(value = "execution(* *.*(..))")
    public void anyMethod() {
    }

    @Pointcut(value = "anyMethod() && @annotation(exitable)")
    public void anyExitableMethod(Exitable exitable) {
    }

    @AfterThrowing(value = "anyExitableMethod(exitable)", throwing = "ex", argNames = "joinPoint,ex,exitable")
    public void onException(final JoinPoint joinPoint, final Exception ex, final Exitable exitable) {
        log.error("{} : @{}{}({})",
                LOG_PREFIX,
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.stream(joinPoint.getArgs())
                        .map(arg -> arg.getClass().getName())
                        .collect(Collectors.joining(",")),
                ex
        );
    }
}
