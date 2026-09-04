package com.phonenumber.validator.dto;

public record PhoneValidationSuccessResponse(
        int statusCode,
        boolean success,
        String message,
        Object data,
        PhoneDetails details
) implements PhoneValidationResponse {
}
