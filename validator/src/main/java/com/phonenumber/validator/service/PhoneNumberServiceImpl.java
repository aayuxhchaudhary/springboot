package com.phonenumber.validator.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.phonenumber.validator.dto.PhoneValidationRequest;
import com.phonenumber.validator.dto.PhoneValidationResponse;
import com.phonenumber.validator.exception.InvalidPhoneNumberException;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private static final PhoneNumberUtil UTIL = PhoneNumberUtil.getInstance();
    private static final Pattern VALID_CHARS = Pattern.compile("^[+]?[0-9\\s\\-\\(\\)\\.]+$");

    @Override
    public PhoneValidationResponse validate(PhoneValidationRequest request) {
        if (request == null) {
            throw new InvalidPhoneNumberException("Request body cannot be null");
        }
        if (request.countryCode() == null || request.countryCode().isBlank()) {
            throw new InvalidPhoneNumberException("Country code cannot be null or empty");
        }
        if (request.phoneNumber() == null || request.phoneNumber().isBlank()) {
            throw new InvalidPhoneNumberException("Phone number cannot be null or empty");
        }

        String region = resolveRegion(request.countryCode().trim());
        String phone = request.phoneNumber().trim();

        if (!VALID_CHARS.matcher(phone).matches()) {
            throw new InvalidPhoneNumberException("Phone number contains invalid non-numeric characters");
        }

        PhoneNumber number = parse(phone, region);
        String countryDisplay = new Locale("", region).getDisplayCountry();

        if (number == null || !UTIL.isValidNumber(number)) {
            throw new InvalidPhoneNumberException(getDetailedReason(number, countryDisplay));
        }

        return new PhoneValidationResponse(true, "Phone number is valid for " + countryDisplay);
    }

    private String resolveRegion(String countryCode) {
        String clean = countryCode.toUpperCase().replace("+", "");
        if (UTIL.getSupportedRegions().contains(clean)) {
            return clean;
        }
        try {
            int code = Integer.parseInt(clean);
            String region = UTIL.getRegionCodeForCountryCode(code);
            if (region != null && !"ZZ".equals(region)) {
                return region;
            }
        } catch (NumberFormatException ignored) {
        }
        throw new InvalidPhoneNumberException("Invalid country code: '" + countryCode + "'");
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
