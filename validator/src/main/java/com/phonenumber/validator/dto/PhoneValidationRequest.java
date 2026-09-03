package com.phonenumber.validator.dto;

public record PhoneValidationRequest(
        String phoneNumber,
        String countryCode
) {
}
