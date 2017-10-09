package com.mycollab.core.utils

import com.mycollab.core.InvalidPasswordException

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object PasswordCheckerUtil {

    private val partialRegexChecks = arrayOf(".*[a-z]+.*", // lower
            ".*[A-Z]+.*", // upper
            ".*[\\d]+.*", // digits
            ".*[@#$%]+.*" // symbols
    )

    @JvmStatic fun checkValidPassword(password: String) {
        if (password.length < 6) {
            throw InvalidPasswordException("Your password is too short - must be at least 6 characters")
        } else {
            checkPasswordStrength(password)
        }
    }

    private fun checkPasswordStrength(password: String) {
        var strengthPercentage = 0

        if (password.matches(partialRegexChecks[0].toRegex())) {
            strengthPercentage += 25
        }
        if (password.matches(partialRegexChecks[1].toRegex())) {
            strengthPercentage += 25
        }
        if (password.matches(partialRegexChecks[2].toRegex())) {
            strengthPercentage += 25
        }
        if (password.matches(partialRegexChecks[3].toRegex())) {
            strengthPercentage += 25
        }

        if (strengthPercentage < 50)
            throw InvalidPasswordException("Password must contain at least one digit letter, one character and one symbol")
    }
}