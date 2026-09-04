package com.phonenumber.validator.dto;

public record PhoneValidationFailureResponse(
        int statusCode,
        boolean success,
        String message
) implements PhoneValidationResponse {
}
