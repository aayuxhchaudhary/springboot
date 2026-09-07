package com.phonenumber.validator.exception;

import com.phonenumber.validator.dto.PhoneValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidPhoneNumberException.class)
    public ResponseEntity<PhoneValidationResponse> handleInvalidPhone(InvalidPhoneNumberException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PhoneValidationResponse.failure(
                400,
                ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PhoneValidationResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(PhoneValidationResponse.failure(
                500,
                "An unexpected error occurred: " + ex.getMessage()
        ));
    }
}
