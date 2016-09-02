/**
 * This file is part of mycollab-localization.
 *
 * mycollab-localization is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-localization is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-localization.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.localization;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("account-billing")
@LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
public enum BillingI18nEnum {
    VIEW_CHANGE_BILLING_PLAN_TITLE,
    BUTTON_CANCEL_ACCOUNT,
    HELP_QUESTION,
    HELP_INFO,
    ACTION_UPGRADE,
    ACTION_DOWNGRADE,
    ACTION_CHANGE_BILLING_INFORMATION,
    ACTION_UPDATE_PAYMENT_METHOD,
    ACTION_SUBMIT_CANCEL_ACCOUNT,
    ACTION_NOT_CANCEL_ACCOUNT,
    OPT_CURRENT_PLAN,
    OPTION_BILLING_FAQ,
    OPT_CANNOT_CHANGE_PLAN,
    OPT_PRICING_MONTH,
    OPT_PLAN_NUM_PROJECTS,
    OPT_PLAN_STORAGE,
    OPT_PLAN_USERS,
    OPT_SUBSCRIPTION_REFERENCE,
    OPT_DISCOUNT_YEARLY_SUBSCRIPTION,
    OPT_SWITCH_MONTHLY_SUBSCRIPTION,
    OPT_EXPIRED_DATE,
    OPT_NEXT_BILLING_DATE,
    OPT_PAYMENT_BANKWIRE,
    OPT_PAYMENT_BANKWIRE_DESC,
    OPT_PAYMENT_BANKWIRE_NOTE,
    OPT_ORDER,
    OPT_PRICE_IN_USD,
    OPT_ACCOUNT_SUSPENDED,
    OPT_PAYMENT_CHARGE_FAILED,
    OPT_FEEDBACK_TITLE,
    OPT_WHY_YOU_LEAVE,
    OPT_CANCEL_AND_OPEN_NEW_ACCOUNT,
    OPT_MISSING_IMPORTANT_FEATURE,
    OPT_TOO_EXPENSIVE,
    OPT_NONE_OF_ABOVE,
    OPT_ANY_APPLY,
    OPT_CONSIDER_OTHER_TOOL,
    OPT_HOW_TO_BACK,
    OPT_CHANGE_CONTACT_INFO,
    FORM_BILLING_PRICE,
    QUESTION_CHANGE_PLAN
}
