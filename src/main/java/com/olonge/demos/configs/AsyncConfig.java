package com.olonge.demos.configs;

import com.olonge.demos.configs.properties.TaskExecutorProperties;
import com.olonge.demos.exceptions.handlers.AsyncExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
@EnableConfigurationProperties({TaskExecutorProperties.class})
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

    private final TaskExecutorProperties taskExecutorProperties;

    /**
     * Application-wide task executor
     * @return
     */
    @Override
    public Executor getAsyncExecutor() {
        return threadPoolTaskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    /**
     * ThreadPool task executor
     * @return
     */
    @Bean
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(taskExecutorProperties.getCorePoolSize());
        executor.setMaxPoolSize(taskExecutorProperties.getMaxPoolSize());
        executor.setQueueCapacity(taskExecutorProperties.getQueueCapacity());
        executor.setThreadNamePrefix(taskExecutorProperties.getThreadNamePrefix());
        executor.initialize();
        return executor;
    }

    /**
     * Concurrent task executor
     * @return
     */
    @Bean
    public Executor concurrentTaskExecutor() {
        return new ConcurrentTaskExecutor(
            Executors.newFixedThreadPool(3)
        );
    }
}
