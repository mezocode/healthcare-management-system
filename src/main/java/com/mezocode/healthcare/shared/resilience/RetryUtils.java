package com.mezocode.healthcare.shared.resilience;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Utility class for retry logic with exponential backoff.
 * Useful for handling transient failures in database operations.
 */
@Slf4j
public class RetryUtils {

    private static final int DEFAULT_MAX_RETRIES = 3;
    private static final long DEFAULT_INITIAL_DELAY_MS = 100;
    private static final double DEFAULT_BACKOFF_MULTIPLIER = 2.0;
    private static final long DEFAULT_MAX_DELAY_MS = 5000;

    /**
     * Execute a callable with retry logic and exponential backoff.
     *
     * @param callable The operation to retry
     * @param operationName Name of the operation for logging
     * @param <T> Return type
     * @return Result of the callable
     * @throws Exception If all retries fail
     */
    public static <T> T executeWithRetry(Callable<T> callable, String operationName) throws Exception {
        return executeWithRetry(callable, operationName, DEFAULT_MAX_RETRIES, DEFAULT_INITIAL_DELAY_MS,
                DEFAULT_BACKOFF_MULTIPLIER, DEFAULT_MAX_DELAY_MS);
    }

    /**
     * Execute a callable with retry logic and exponential backoff.
     *
     * @param callable The operation to retry
     * @param operationName Name of the operation for logging
     * @param maxRetries Maximum number of retry attempts
     * @param initialDelayMs Initial delay in milliseconds
     * @param backoffMultiplier Multiplier for exponential backoff
     * @param maxDelayMs Maximum delay between retries in milliseconds
     * @param <T> Return type
     * @return Result of the callable
     * @throws Exception If all retries fail
     */
    public static <T> T executeWithRetry(Callable<T> callable, String operationName,
                                         int maxRetries, long initialDelayMs,
                                         double backoffMultiplier, long maxDelayMs) throws Exception {
        Exception lastException = null;
        long delay = initialDelayMs;

        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                if (attempt > 0) {
                    log.debug("Retry attempt {} for operation: {}", attempt, operationName);
                }
                return callable.call();
            } catch (Exception e) {
                lastException = e;
                if (attempt < maxRetries) {
                    log.warn("Operation {} failed on attempt {}: {}. Retrying in {}ms",
                            operationName, attempt + 1, e.getMessage(), delay);
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                    delay = Math.min((long) (delay * backoffMultiplier), maxDelayMs);
                } else {
                    log.error("Operation {} failed after {} attempts", operationName, maxRetries + 1, e);
                }
            }
        }

        throw lastException != null ? lastException : new RuntimeException("Operation failed: " + operationName);
    }

    /**
     * Execute a supplier with retry logic and exponential backoff.
     *
     * @param supplier The operation to retry
     * @param operationName Name of the operation for logging
     * @param <T> Return type
     * @return Result of the supplier
     */
    public static <T> T executeWithRetry(Supplier<T> supplier, String operationName) {
        try {
            return executeWithRetry((Callable<T>) supplier::get, operationName);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException("Operation failed: " + operationName, e);
        }
    }

    /**
     * Execute a runnable with retry logic and exponential backoff.
     *
     * @param runnable The operation to retry
     * @param operationName Name of the operation for logging
     */
    public static void executeWithRetry(Runnable runnable, String operationName) {
        try {
            executeWithRetry((Callable<Void>) () -> {
                runnable.run();
                return null;
            }, operationName);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException("Operation failed: " + operationName, e);
        }
    }
}

