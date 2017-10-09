package com.mycollab.mobile.ui;

import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class FormSectionBuilder {
    public static MHorizontalLayout build(FontAwesome icon, Component comp) {
        MHorizontalLayout layout = new MHorizontalLayout().withFullWidth().withStyleName(MobileUIConstants.FORM_SECTION);
        layout.with(ELabel.fontIcon(icon), comp).expand(comp).alignAll(Alignment.MIDDLE_LEFT);
        return layout;
    }

    public static MHorizontalLayout build(FontAwesome icon, Enum messageEnum) {
        return build(icon, new Label(UserUIContext.getMessage(messageEnum)));
    }

    public static MCssLayout build(String title) {
        Label header = new Label(title);
        return new MCssLayout(header).withFullWidth().withStyleName(MobileUIConstants.FORM_SECTION);
    }
}
