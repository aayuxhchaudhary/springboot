package com.phonenumber.validator.controller;

import com.phonenumber.validator.dto.PhoneValidationResponse;
import com.phonenumber.validator.service.PhoneNumberService;
import com.phonenumber.validator.util.PhoneNumberRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhoneNumberController {

    private final PhoneNumberService service;

    public PhoneNumberController(PhoneNumberService service) {
        this.service = service;
    }

    @PostMapping("/validate")
    public ResponseEntity<PhoneValidationResponse> validate(@RequestBody PhoneNumberRequest request) {
        PhoneValidationResponse response = service.validate(request);
        return ResponseEntity.status(response.statusCode()).body(response);
    }
}
