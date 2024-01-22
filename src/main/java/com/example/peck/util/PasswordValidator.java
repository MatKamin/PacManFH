package com.example.peck.util;

import java.util.regex.Pattern;

/**
 * Utility class for validating passwords based on specified criteria.
 */
public class PasswordValidator {

    /**
     * Validates a password based on the following criteria:
     *     Minimum 8 characters in length.
     *     At least one digit.
     *     At least one lowercase letter.
     *     At least one uppercase letter.
     *     At least one special character (includes @, #, $, %, ^, &, +, =, .)
     *     No whitespace characters allowed.
     *
     *
     * @param password The password string to be validated.
     * @return {@code true} if the password meets the criteria; {@code false} otherwise.
     */
    public static boolean isPasswordValid(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,}$";
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }
}
