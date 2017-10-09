package com.mycollab.vaadin.ui.field;

import com.mycollab.core.utils.CurrencyUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

import java.util.Currency;

/**
 * @author MyCollab Ltd
 * @since 5.3.1
 */
public class CurrencyViewField extends CustomField<String> {
    private ELabel label;

    public CurrencyViewField(String value) {
        if (StringUtils.isBlank(value)) {
            label = new ELabel("");
        } else {
            Currency currency = CurrencyUtils.getInstance(value);
            label = new ELabel(String.format("%s (%s)", currency.getDisplayName(UserUIContext.getUserLocale()), currency.getCurrencyCode()))
                    .withFullWidth().withStyleName(UIConstants.LABEL_WORD_WRAP);
        }
    }

    @Override
    protected Component initContent() {
        return label;
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }
}
