package com.mycollab.community.vaadin.web.ui.field;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.server.FontIcon;
import com.vaadin.ui.AbstractComponent;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class MetaFieldBuilder {
    private String captionHtml;
    private String description = "Edit";

    public MetaFieldBuilder withCaptionAndIcon(FontIcon icon, String caption) {
        captionHtml = icon.getHtml() + " " + StringUtils.trim(caption, 20, true);
        return this;
    }

    public MetaFieldBuilder withCaption(String caption) {
        this.captionHtml = caption;
        return this;
    }

    public MetaFieldBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public AbstractComponent build() {
        return ELabel.html(captionHtml).withDescription(description).withStyleName("block-popupedit").withWidthUndefined();
    }
}
