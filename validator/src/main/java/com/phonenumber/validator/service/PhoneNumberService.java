package com.phonenumber.validator.service;

import com.phonenumber.validator.dto.PhoneValidationRequest;
import com.phonenumber.validator.dto.PhoneValidationResponse;

public interface PhoneNumberService {
    PhoneValidationResponse validate(PhoneValidationRequest request);
}
