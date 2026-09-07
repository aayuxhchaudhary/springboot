package com.phonenumber.validator.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PhoneValidationResponse(
        int statusCode,
        boolean success,
        String error,
        String message,
        Object data,
        Object details
) {
    public static PhoneValidationResponse success(int statusCode, String message, PhoneData data, Object details) {
        return new PhoneValidationResponse(statusCode, true, null, message, data, details);
    }

    public static PhoneValidationResponse failure(int statusCode, String message) {
        String error = HttpStatus.valueOf(statusCode).getReasonPhrase();
        return new PhoneValidationResponse(statusCode, false, error, message, null, null);
    }
}
