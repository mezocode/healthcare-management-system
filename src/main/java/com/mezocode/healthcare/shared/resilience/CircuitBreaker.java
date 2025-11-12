package com.mezocode.healthcare.shared.resilience;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Simple circuit breaker implementation for database operations.
 *
 * <p>The circuit breaker has three states:
 * <ul>
 *   <li>CLOSED: Normal operation, requests pass through</li>
 *   <li>OPEN: Circuit is open, requests fail fast</li>
 *   <li>HALF_OPEN: Testing if service has recovered</li>
 * </ul>
 */
@Slf4j
public class CircuitBreaker {

    public enum State {
        CLOSED, OPEN, HALF_OPEN
    }

    @Getter
    private final String name;
    private final int failureThreshold;
    private final long timeoutMs;
    private final long halfOpenTimeoutMs;

    private final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicLong lastFailureTime = new AtomicLong(0);
    private final AtomicLong openedAt = new AtomicLong(0);

    /**
     * Create a circuit breaker with default settings.
     *
     * @param name Name of the circuit breaker
     */
    public CircuitBreaker(String name) {
        this(name, 5, 60000, 30000);
    }

    /**
     * Create a circuit breaker with custom settings.
     *
     * @param name Name of the circuit breaker
     * @param failureThreshold Number of failures before opening circuit
     * @param timeoutMs Time in milliseconds before attempting to close circuit
     * @param halfOpenTimeoutMs Time in milliseconds to wait in half-open state
     */
    public CircuitBreaker(String name, int failureThreshold, long timeoutMs, long halfOpenTimeoutMs) {
        this.name = name;
        this.failureThreshold = failureThreshold;
        this.timeoutMs = timeoutMs;
        this.halfOpenTimeoutMs = halfOpenTimeoutMs;
    }

    /**
     * Execute an operation with circuit breaker protection.
     *
     * @param operation The operation to execute
     * @param <T> Return type
     * @return Result of the operation
     * @throws CircuitBreakerOpenException If circuit is open
     * @throws RuntimeException If operation fails
     */
    public <T> T execute(Supplier<T> operation) {
        State currentState = state.get();

        if (currentState == State.OPEN) {
            if (shouldAttemptReset()) {
                if (state.compareAndSet(State.OPEN, State.HALF_OPEN)) {
                    log.info("Circuit breaker [{}] transitioning to HALF_OPEN state", name);
                    currentState = State.HALF_OPEN;
                } else {
                    throw new CircuitBreakerOpenException("Circuit breaker [" + name + "] is OPEN");
                }
            } else {
                throw new CircuitBreakerOpenException("Circuit breaker [" + name + "] is OPEN");
            }
        }

        try {
            T result = operation.get();
            onSuccess();
            return result;
        } catch (Exception e) {
            onFailure();
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
        }
    }

    /**
     * Execute an operation that may throw checked exceptions.
     *
     * @param operation The operation to execute
     * @param <T> Return type
     * @return Result of the operation
     * @throws Exception If operation fails or circuit is open
     */
    public <T> T executeChecked(java.util.concurrent.Callable<T> operation) throws Exception {
        State currentState = state.get();

        if (currentState == State.OPEN) {
            if (shouldAttemptReset()) {
                if (state.compareAndSet(State.OPEN, State.HALF_OPEN)) {
                    log.info("Circuit breaker [{}] transitioning to HALF_OPEN state", name);
                    currentState = State.HALF_OPEN;
                } else {
                    throw new CircuitBreakerOpenException("Circuit breaker [" + name + "] is OPEN");
                }
            } else {
                throw new CircuitBreakerOpenException("Circuit breaker [" + name + "] is OPEN");
            }
        }

        try {
            T result = operation.call();
            onSuccess();
            return result;
        } catch (Exception e) {
            onFailure();
            throw e;
        }
    }

    private void onSuccess() {
        State currentState = state.get();
        if (currentState == State.HALF_OPEN) {
            if (state.compareAndSet(State.HALF_OPEN, State.CLOSED)) {
                log.info("Circuit breaker [{}] transitioning to CLOSED state (recovered)", name);
                failureCount.set(0);
                lastFailureTime.set(0);
            }
        } else if (currentState == State.CLOSED) {
            failureCount.set(0);
        }
    }

    private void onFailure() {
        lastFailureTime.set(Instant.now().toEpochMilli());
        int failures = failureCount.incrementAndGet();

        State currentState = state.get();
        if (currentState == State.HALF_OPEN) {
            // Failed in half-open, go back to open
            if (state.compareAndSet(State.HALF_OPEN, State.OPEN)) {
                log.warn("Circuit breaker [{}] transitioning to OPEN state (failed in HALF_OPEN)", name);
                openedAt.set(Instant.now().toEpochMilli());
            }
        } else if (currentState == State.CLOSED && failures >= failureThreshold) {
            // Too many failures, open the circuit
            if (state.compareAndSet(State.CLOSED, State.OPEN)) {
                log.error("Circuit breaker [{}] transitioning to OPEN state ({} failures)", name, failures);
                openedAt.set(Instant.now().toEpochMilli());
            }
        }
    }

    private boolean shouldAttemptReset() {
        long opened = openedAt.get();
        if (opened == 0) {
            return false;
        }
        long elapsed = Instant.now().toEpochMilli() - opened;
        return elapsed >= timeoutMs;
    }

    /**
     * Get current state of the circuit breaker.
     *
     * @return Current state
     */
    public State getState() {
        return state.get();
    }

    /**
     * Get current failure count.
     *
     * @return Failure count
     */
    public int getFailureCount() {
        return failureCount.get();
    }

    /**
     * Reset the circuit breaker to CLOSED state (for testing/admin purposes).
     */
    public void reset() {
        state.set(State.CLOSED);
        failureCount.set(0);
        lastFailureTime.set(0);
        openedAt.set(0);
        log.info("Circuit breaker [{}] manually reset to CLOSED state", name);
    }

    /**
     * Exception thrown when circuit breaker is open.
     */
    public static class CircuitBreakerOpenException extends RuntimeException {
        public CircuitBreakerOpenException(String message) {
            super(message);
        }
    }
}

