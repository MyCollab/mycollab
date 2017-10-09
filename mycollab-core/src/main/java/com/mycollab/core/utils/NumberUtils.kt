package com.mycollab.core.utils

import java.math.BigDecimal

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
object NumberUtils {
    @JvmStatic
    fun roundDouble(scale: Int, value: Double?): Double =
            when (value) {
                null -> 0.0
                else -> BigDecimal(value.toString()).setScale(scale, BigDecimal.ROUND_HALF_UP).toDouble()
            }

    @JvmStatic
    fun zeroIfNull(value: Number?): Number = value ?: 0
}
