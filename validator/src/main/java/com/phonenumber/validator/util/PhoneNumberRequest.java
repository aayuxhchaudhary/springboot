package com.phonenumber.validator.util;

public class PhoneNumberRequest {

    private String countryCode;
    private String phoneNumber;

    public PhoneNumberRequest() {
    }

    public PhoneNumberRequest(String countryCode, String phoneNumber) {
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
