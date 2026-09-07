package com.phonenumber.validator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PhoneData(
        String countryCode,
        String phoneNumber,
        String normalizedPhoneNumber,
        @JsonProperty("is_verified") boolean isVerified
) {
    public PhoneData(String countryCode, String phoneNumber, String normalizedPhoneNumber) {
        this(countryCode, phoneNumber, normalizedPhoneNumber, true);
    }
}
