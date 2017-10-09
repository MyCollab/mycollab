package com.mycollab.vaadin.ui.field;

import com.hp.gagawa.java.elements.A;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class UrlLinkViewField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private String url;
    private String caption;

    public UrlLinkViewField(String url) {
        this(url, url);
    }

    public UrlLinkViewField(String url, String caption) {
        this.url = url;
        this.caption = caption;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    protected Component initContent() {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(caption)) {
            return new Label("&nbsp;", ContentMode.HTML);
        } else {
            final A link = new A(url).appendText(caption).setTarget("_blank");
            return ELabel.html(link.write()).withStyleName(UIConstants.TEXT_ELLIPSIS);
        }
    }
}
