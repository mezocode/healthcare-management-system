package com.mezocode.healthcare.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class RestErrorMessage {

    @JsonProperty("error_code")
    private int responseStatus;
    private String message;

    public static RestErrorMessage of(String message, HttpStatus httpStatus) {
        return RestErrorMessage
                .builder()
                .message(message)
                .responseStatus(httpStatus.value())
                .build();
    }

    public static RestErrorMessage of(String message, int httpStatus) {
        return RestErrorMessage
                .builder()
                .message(message)
                .responseStatus(httpStatus)
                .build();
    }

    public static RestErrorMessage ofDefault(String message) {
        return of(message, BAD_REQUEST);
    }
}
