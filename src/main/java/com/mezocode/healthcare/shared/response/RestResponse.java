package com.mezocode.healthcare.shared.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface RestResponse<T> {
}
