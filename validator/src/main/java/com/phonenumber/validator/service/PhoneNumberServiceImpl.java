package com.phonenumber.validator.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.phonenumber.validator.dto.PhoneDetails;
import com.phonenumber.validator.dto.PhoneValidationFailureResponse;
import com.phonenumber.validator.dto.PhoneValidationResponse;
import com.phonenumber.validator.dto.PhoneValidationSuccessResponse;
import com.phonenumber.validator.exception.InvalidPhoneNumberException;
import com.phonenumber.validator.util.PhoneNumberRequest;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private static final PhoneNumberUtil UTIL = PhoneNumberUtil.getInstance();
    private static final Pattern VALID_CHARS = Pattern.compile("^[+]?[0-9\\s\\-\\(\\)\\.]+$");
    private static final Pattern NUMERIC_COUNTRY_CODE = Pattern.compile("^[+]?[0-9]+$");

    @Override
    public PhoneValidationResponse validate(PhoneNumberRequest request) {
        String rawCountry = (request != null) ? request.getCountryCode() : null;
        String rawPhone = (request != null) ? request.getPhoneNumber() : null;

        try {
            if (request == null) {
                throw new InvalidPhoneNumberException("Request body cannot be null");
            }
            if (rawCountry == null || rawCountry.isBlank()) {
                throw new InvalidPhoneNumberException("Country code cannot be null or empty");
            }
            if (rawPhone == null || rawPhone.isBlank()) {
                throw new InvalidPhoneNumberException("Phone number cannot be null or empty");
            }

            String countryInput = rawCountry.trim();
            if (!NUMERIC_COUNTRY_CODE.matcher(countryInput).matches()) {
                throw new InvalidPhoneNumberException("Invalid country code: '" + countryInput + "'. Only numeric calling codes (e.g. +91, 91, +1) are accepted. ISO country codes are not allowed.");
            }

            String region = resolveRegionFromCallingCode(countryInput);
            String phone = rawPhone.trim();

            if (!VALID_CHARS.matcher(phone).matches()) {
                throw new InvalidPhoneNumberException("Phone number contains invalid non-numeric characters");
            }

            PhoneNumber number = parse(phone, region);
            String countryDisplay = new Locale("", region).getDisplayCountry();

            if (number == null || !UTIL.isValidNumber(number)) {
                throw new InvalidPhoneNumberException(getDetailedReason(number, countryDisplay));
            }

            String normalizedNumber = UTIL.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
            String formattedCountryCode = countryInput.startsWith("+") ? countryInput : "+" + countryInput;

            PhoneDetails details = new PhoneDetails(formattedCountryCode, phone, normalizedNumber);

            return new PhoneValidationSuccessResponse(
                    200,
                    true,
                    "Phone number is valid for " + countryDisplay,
                    null,
                    details
            );

        } catch (InvalidPhoneNumberException ex) {
            return new PhoneValidationFailureResponse(
                    400,
                    false,
                    ex.getMessage()
            );

        } catch (Exception ex) {
            return new PhoneValidationFailureResponse(
                    500,
                    false,
                    "An unexpected error occurred: " + ex.getMessage()
            );
        }
    }

    private String resolveRegionFromCallingCode(String countryCode) {
        try {
            int code = Integer.parseInt(countryCode.replace("+", ""));
            String region = UTIL.getRegionCodeForCountryCode(code);
            if (region != null && !"ZZ".equals(region)) {
                return region;
            }
        } catch (NumberFormatException ignored) {
        }
        throw new InvalidPhoneNumberException("Unknown or invalid country calling code: '" + countryCode + "'");
    }

    private PhoneNumber parse(String phone, String region) {
        int expectedCallingCode = UTIL.getCountryCodeForRegion(region);
        try {
            if (phone.startsWith("+")) {
                PhoneNumber parsed = UTIL.parse(phone, region);
                if (parsed.getCountryCode() != expectedCallingCode) {
                    throw new InvalidPhoneNumberException("Phone number dial code (+" + parsed.getCountryCode() + ") does not match provided country code '" + region + "'");
                }
                return parsed;
            }

            PhoneNumber direct = UTIL.parse(phone, region);
            if (UTIL.isValidNumber(direct)) {
                return direct;
            }

            try {
                PhoneNumber withPlus = UTIL.parse("+" + phone, region);
                if (withPlus.getCountryCode() == expectedCallingCode && UTIL.isValidNumber(withPlus)) {
                    return withPlus;
                }
            } catch (NumberParseException ignored) {
            }

            return direct;
        } catch (NumberParseException e) {
            throw new InvalidPhoneNumberException("Could not parse phone number '" + phone + "': " + e.getMessage());
        }
    }

    private String getDetailedReason(PhoneNumber number, String countryDisplay) {
        if (number == null) {
            return "Phone number format is invalid for " + countryDisplay;
        }
        int len = String.valueOf(number.getNationalNumber()).length();
        PhoneNumberUtil.ValidationResult result = UTIL.isPossibleNumberWithReason(number);
        return switch (result) {
            case TOO_SHORT -> "Phone number is too short for " + countryDisplay + " (found " + len + " digits)";
            case TOO_LONG -> "Phone number is too long for " + countryDisplay + " (found " + len + " digits)";
            case INVALID_LENGTH -> "Phone number has invalid length for " + countryDisplay + " (found " + len + " digits)";
            default -> "Phone number is invalid for " + countryDisplay;
        };
    }
}
