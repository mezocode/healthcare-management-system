package com.mezocode.healthcare.response;

import java.util.List;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpHeaders.EMPTY;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@NoArgsConstructor(access = PRIVATE)
public class RestCollector {
    public static ResponseEntity<RestResponse<Responseable>> noContentResponse() {
        return ResponseEntity.status(NO_CONTENT)
                .headers(getResponseHeaders())
                .build();
    }


    @SuppressWarnings("unchecked")
    public static ResponseEntity<RestResponse<Responseable>> toResponseBody(Object object, HttpStatus httpStatus) {

        if (object instanceof Responseable) {
            return toSingleResponse((Responseable) object, httpStatus);
        } else if (object instanceof List) {
            return toListResponse((List<Responseable>) object, httpStatus);
        } else if (object instanceof Page) {
            return toPagedResponse((Page<Responseable>) object, httpStatus);
        } else {
            return noContentResponse();
        }
    }

    public static ResponseEntity<Object> toResponseBody(RestErrorMessage restErrorMessage) {
        return ResponseEntity
                .status(restErrorMessage.getResponseStatus())
                .headers(getResponseHeaders())
                .body(restErrorMessage);
    }

    private static ResponseEntity<RestResponse<Responseable>> toSingleResponse(Responseable responseDto,
                                                                               HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .headers(getResponseHeaders())
                .body(SingleRestResponse.responseBody(responseDto));
    }

    private static ResponseEntity<RestResponse<Responseable>> toPagedResponse(@NonNull Page<? extends Responseable> pageable,
                                                                              HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .headers(getResponseHeaders())
                .body(PagedRestResponse.responseBody(pageable));
    }

    private static ResponseEntity<RestResponse<Responseable>> toListResponse(@NonNull List<? extends Responseable> dataList,
                                                                             HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .headers(getResponseHeaders())
                .body(ListRestResponse.responseBody(dataList));
    }

    private static HttpHeaders getResponseHeaders() {
        return EMPTY;
    }
}
