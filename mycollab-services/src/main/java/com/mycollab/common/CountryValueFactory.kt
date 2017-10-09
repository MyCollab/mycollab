package com.mycollab.common

import java.util.Locale

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object CountryValueFactory {
    @JvmField var countryList = Locale.getISOCountries()
}
