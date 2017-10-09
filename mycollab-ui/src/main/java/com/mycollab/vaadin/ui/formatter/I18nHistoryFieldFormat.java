package com.mycollab.vaadin.ui.formatter;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public class I18nHistoryFieldFormat implements HistoryFieldFormat {
    private Class<? extends Enum> enumCls;

    public I18nHistoryFieldFormat(Class<? extends Enum> enumCls) {
        this.enumCls = enumCls;
    }

    @Override
    public String toString(String value) {
        return toString(value, true, "");
    }

    @Override
    public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
        return (StringUtils.isNotBlank(value)) ? UserUIContext.getMessage(enumCls, value) : msgIfBlank;
    }
}
