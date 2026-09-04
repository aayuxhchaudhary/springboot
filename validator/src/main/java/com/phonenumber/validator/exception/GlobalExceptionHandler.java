package com.phonenumber.validator.exception;

import com.phonenumber.validator.dto.PhoneValidationFailureResponse;
import com.phonenumber.validator.dto.PhoneValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidPhoneNumberException.class)
    public ResponseEntity<PhoneValidationResponse> handleInvalidPhone(InvalidPhoneNumberException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PhoneValidationFailureResponse(
                400,
                false,
                ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PhoneValidationResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PhoneValidationFailureResponse(
                500,
                false,
                "An unexpected error occurred: " + ex.getMessage()
        ));
    }
}
