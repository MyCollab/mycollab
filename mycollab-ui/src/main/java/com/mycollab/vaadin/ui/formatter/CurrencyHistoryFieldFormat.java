package com.mycollab.vaadin.ui.formatter;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.CurrencyUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;
import com.hp.gagawa.java.elements.Span;

import java.util.Currency;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public class CurrencyHistoryFieldFormat implements HistoryFieldFormat {

    @Override
    public String toString(String value) {
        return toString(value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY));
    }

    @Override
    public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
        if (StringUtils.isNotBlank(value)) {
            Currency currency = CurrencyUtils.getInstance(value);
            if (displayAsHtml) {
                return new Span().appendText(value).setTitle(currency.getDisplayName(UserUIContext.getUserLocale())).write();
            } else {
                return value;
            }
        } else {
            return msgIfBlank;
        }
    }
}
