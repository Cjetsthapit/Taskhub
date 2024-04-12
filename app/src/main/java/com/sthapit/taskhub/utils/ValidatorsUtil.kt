package com.sthapit.taskhub.utils

class ValidatorsUtil {


    companion object {
        fun isValidCardNumber(number: String): Boolean {
            // Dummy check for length, adapt regex or use a library like Stripe for comprehensive validation
            return number.length == 16
        }

        fun isValidExpiry(month: String, year: String): Boolean {
            // Ensure month and year form a valid future date
            // For simplicity, just checking length here
            return month.length == 2 && year.length == 4
        }

        fun isValidCVV(cvv: String): Boolean {
            // CVV typically should be 3 or 4 digits
            return cvv.length == 3 || cvv.length == 4
        }
    }
}