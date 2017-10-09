package com.mycollab.vaadin.ui.formatter;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public class LocalizationHistoryFieldFormat implements HistoryFieldFormat {
    private Class<? extends Enum> enumCls;

    public LocalizationHistoryFieldFormat(Class<? extends Enum> enumCls) {
        this.enumCls = enumCls;
    }

    @Override
    public String toString(String value) {
        return toString(value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY));
    }

    @Override
    public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
        String content;
        if (StringUtils.isNotBlank(value)) {
            content = UserUIContext.getMessage(enumCls, value);
            content = (content.length() > 150) ? (content.substring(0, 150) + "...") : content;
        } else {
            content = msgIfBlank;
        }

        return content;
    }
}
