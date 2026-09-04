package com.phonenumber.validator.dto;

public record PhoneDetails(
        String countryCode,
        String phoneNumber,
        String normalizedPhoneNumber
) {
}
