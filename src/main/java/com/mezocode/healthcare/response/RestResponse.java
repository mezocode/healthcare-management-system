package com.mezocode.healthcare.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface RestResponse<T> {
}
