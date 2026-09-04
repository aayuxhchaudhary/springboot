package com.phonenumber.validator.dto;

public sealed interface PhoneValidationResponse permits PhoneValidationSuccessResponse, PhoneValidationFailureResponse {
    int statusCode();
    boolean success();
    String message();
}
