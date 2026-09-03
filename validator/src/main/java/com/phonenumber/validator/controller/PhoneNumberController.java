package com.phonenumber.validator.controller;

import com.phonenumber.validator.dto.PhoneValidationRequest;
import com.phonenumber.validator.dto.PhoneValidationResponse;
import com.phonenumber.validator.service.PhoneNumberService;
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
    public ResponseEntity<PhoneValidationResponse> validate(@RequestBody PhoneValidationRequest request) {
        return ResponseEntity.ok(service.validate(request));
    }
}
