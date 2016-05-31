/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.user.domain;

import com.esofthead.mycollab.core.arguments.NotBindable;
import com.esofthead.mycollab.core.utils.CurrencyUtils;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.google.common.base.MoreObjects;

import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class SimpleBillingAccount extends BillingAccount {
    private static final long serialVersionUID = 1L;

    @NotBindable
    private BillingPlan billingPlan;

    @NotBindable
    private SimpleDateFormat dateTimeFormatInstance;

    @NotBindable
    private SimpleDateFormat dateFormatInstance;

    @NotBindable
    private SimpleDateFormat shortDateFormatInstance;

    @NotBindable
    private SimpleDateFormat longDateFormatInstance;

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

    public SimpleDateFormat getDateFormatInstance() {
        if (dateFormatInstance == null) {
            dateFormatInstance = new SimpleDateFormat(MoreObjects.firstNonNull(getDefaultyymmddformat(), "MM/dd/yyyy"));
        }
        return dateFormatInstance;
    }

    public SimpleDateFormat getShortDateFormatInstance() {
        if (shortDateFormatInstance == null) {
            shortDateFormatInstance = new SimpleDateFormat(MoreObjects.firstNonNull(getDefaultmmddformat(), "MM/dd"));
        }
        return shortDateFormatInstance;
    }

    public SimpleDateFormat getLongDateFormatInstance() {
        if (longDateFormatInstance == null) {
            longDateFormatInstance = new SimpleDateFormat(MoreObjects.firstNonNull(getDefaulthumandateformat(), "E, dd MMM yyyy"));
        }
        return longDateFormatInstance;
    }

    public SimpleDateFormat getDateTimeFormatInstance() {
        if (dateTimeFormatInstance == null) {
            String defaultTimeFormat = MoreObjects.firstNonNull(getDefaultyymmddformat(), "MM/dd/yyyy");
            dateTimeFormatInstance = new SimpleDateFormat(defaultTimeFormat + " HH:mm:ss Z");
        }
        return dateTimeFormatInstance;
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
}
