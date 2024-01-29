package com.mezocode.healthcare.shared.annotation;

@FunctionalInterface
public interface ApiCallable<T> {
    T call();
}
