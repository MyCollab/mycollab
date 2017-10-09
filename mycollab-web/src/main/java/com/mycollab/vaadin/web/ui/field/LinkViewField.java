package com.mycollab.vaadin.web.ui.field;

import com.mycollab.vaadin.web.ui.LabelLink;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.StringUtils;

/**
 * @author MyCollab Ltd.
 * @since 4.5.3
 */
public class LinkViewField extends CustomField<String> {
    private static final long serialVersionUID = 1L;

    private String value;
    private Object iconResourceLink;
    private String href;

    public LinkViewField(String value, String href) {
        this(value, href, null);
    }

    public LinkViewField(String value, String href, Object iconResourceLink) {
        this.value = value;
        this.href = href;
        this.iconResourceLink = iconResourceLink;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    protected Component initContent() {
        if (StringUtils.isNotBlank(value)) {
            final LabelLink l = new LabelLink(value, href);
            if (iconResourceLink != null) {
                l.setIconLink(iconResourceLink);
            }
            l.setWidth("100%");
            return l;
        } else {
            final Label l = new Label("&nbsp;", ContentMode.HTML);
            l.setWidth("100%");
            return l;
        }
    }
}
