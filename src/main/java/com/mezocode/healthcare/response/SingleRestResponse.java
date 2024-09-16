package com.mezocode.healthcare.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class SingleRestResponse<T extends Responseable> implements RestResponse<T> {
    private final T data;

    private SingleRestResponse(T data) {
        this.data = data;
    }

    public static <T extends Responseable> SingleRestResponse<T> responseBody(@NonNull T data) {
        return new SingleRestResponse<>(data);
    }
}
