package com.phonenumber.validator.service;

import com.phonenumber.validator.dto.PhoneValidationResponse;
import com.phonenumber.validator.util.PhoneNumberRequest;

public interface PhoneNumberService {
    PhoneValidationResponse validate(PhoneNumberRequest request);
}
