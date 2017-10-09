package com.mycollab.vaadin.ui.formatter;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;

import java.util.Date;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public class DateHistoryFieldFormat implements HistoryFieldFormat {

    @Override
    public String toString(String value) {
        return toString(value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY));
    }

    @Override
    public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
        String content;
        if (StringUtils.isNotBlank(value)) {
            Date formatDate = DateTimeUtils.parseDateByW3C(value);
            content = UserUIContext.formatDate(formatDate);
        } else {
            content = msgIfBlank;
        }
        return content;
    }
}
