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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.domain;

import com.mycollab.core.arguments.NotBindable;
import com.mycollab.core.utils.CurrencyUtils;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.billing.AccountStatusConstants;
import com.google.common.base.MoreObjects;

import java.util.Currency;
import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleBillingAccount extends BillingAccount {
    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
    public static final String DEFAULT_SHORT_DATE_FORMAT = "MM/dd";
    public static final String DEFAULT_LONG_DATE_FORMAT = "E, dd MMM yyyy";

    @NotBindable
    private BillingPlan billingPlan;

    @NotBindable
    private Currency currencyInstance;

    @NotBindable
    private Locale localeInstance;

    public BillingPlan getBillingPlan() {
        return billingPlan;
    }

    public void setBillingPlan(BillingPlan billingPlan) {
        this.billingPlan = billingPlan;
    }

    @Override
    public void setDefaultlanguagetag(String defaultlanguagetag) {
        super.setDefaultlanguagetag(defaultlanguagetag);
    }

    public String getDateFormatInstance() {
        return MoreObjects.firstNonNull(getDefaultyymmddformat(), DEFAULT_DATE_FORMAT);
    }

    public String getShortDateFormatInstance() {
        return MoreObjects.firstNonNull(getDefaultmmddformat(), DEFAULT_SHORT_DATE_FORMAT);
    }

    public String getLongDateFormatInstance() {
        return MoreObjects.firstNonNull(getDefaulthumandateformat(), DEFAULT_LONG_DATE_FORMAT);
    }

    public String getDateTimeFormatInstance() {
        return MoreObjects.firstNonNull(getDefaultyymmddformat(), DEFAULT_DATE_FORMAT) + " KK:mm a";
    }

    public Locale getLocaleInstance() {
        if (localeInstance == null) {
            localeInstance = LocalizationHelper.getLocaleInstance(getDefaultlanguagetag());
        }
        return localeInstance;
    }

    public Currency getCurrencyInstance() {
        if (currencyInstance == null) {
            currencyInstance = CurrencyUtils.getInstance(getDefaultcurrencyid());
        }
        return currencyInstance;
    }

    public Boolean isNotActive() {
        return !AccountStatusConstants.ACTIVE.equals(getStatus());
    }
}
