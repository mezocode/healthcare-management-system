package com.mezocode.healthcare.shared;

import com.mezocode.healthcare.exception.NotFoundException;
import com.mezocode.healthcare.exception.ServiceUnavailableException;
import com.mezocode.healthcare.exception.UnauthorizedException;
import com.mezocode.healthcare.shared.annotation.ApiCallable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    /*
    *
    * return CustomExceptionHandler.handleApiCall(() -> clientFacade.getProfile(correlationId, userAgent, authorization));
    *
    **/

    public static <T> T handleApiCall(ApiCallable<T> apiCallable) {
        try {
            return apiCallable.call();
        } catch (UnauthorizedException e) {
            logger.error("Unauthorized access", e);
            throw e;
        } catch (NotFoundException e) {
            logger.error("Resource not found", e);
            throw e;
        } catch (ServiceUnavailableException e) {
            logger.error("Service unavailable", e);
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred", e);
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }

}