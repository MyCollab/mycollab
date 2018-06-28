/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.domain

import com.google.common.base.MoreObjects
import com.mycollab.core.arguments.NotBindable
import com.mycollab.core.utils.CurrencyUtils
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.billing.AccountStatusConstants
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class SimpleBillingAccount : BillingAccount() {

    @NotBindable
    var billingPlan: BillingPlan? = null

    @NotBindable
    var currencyInstance: Currency? = null
        get() = if (field == null) {
            CurrencyUtils.getInstance(defaultcurrencyid)
        } else field

    @NotBindable
    var localeInstance: Locale? = null
        get() = if (field == null) {
            LocalizationHelper.getLocaleInstance(defaultlanguagetag)
        } else field

    val dateFormatInstance: String
        get() = MoreObjects.firstNonNull(defaultyymmddformat, DEFAULT_DATE_FORMAT)

    val shortDateFormatInstance: String
        get() = MoreObjects.firstNonNull(defaultmmddformat, DEFAULT_SHORT_DATE_FORMAT)

    val longDateFormatInstance: String
        get() = MoreObjects.firstNonNull(defaulthumandateformat, DEFAULT_LONG_DATE_FORMAT)

    val dateTimeFormatInstance: String
        get() = MoreObjects.firstNonNull(defaultyymmddformat, DEFAULT_DATE_FORMAT) + " KK:mm a"

    val isNotActive: Boolean?
        get() = AccountStatusConstants.ACTIVE != status

    override fun setDefaultlanguagetag(defaultlanguagetag: String) {
        super.setDefaultlanguagetag(defaultlanguagetag)
    }

    companion object {
        private val serialVersionUID = 1L

        @JvmField
        val DEFAULT_DATE_FORMAT = "MM/dd/yyyy"
        @JvmField
        val DEFAULT_SHORT_DATE_FORMAT = "MM/dd"
        @JvmField
        val DEFAULT_LONG_DATE_FORMAT = "E, dd MMM yyyy"
    }
}
