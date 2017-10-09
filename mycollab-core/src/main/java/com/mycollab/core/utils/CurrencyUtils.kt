package com.mycollab.core.utils

import java.util.Currency
import java.util.Locale

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
object CurrencyUtils {
    @JvmStatic fun getInstance(currencyCode: String?): Currency {
        return try {
            Currency.getInstance(currencyCode)
        } catch (e: Exception) {
            Currency.getInstance(Locale.US)
        }
    }
}
