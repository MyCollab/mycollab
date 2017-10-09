package com.mycollab.vaadin.web.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class AbstractToggleSummaryField extends CssLayout {
    protected Label titleLinkLbl;
    protected MHorizontalLayout buttonControls;

    public void addLabelStyleName(String styleName) {
        titleLinkLbl.addStyleName(styleName);
    }

    public void removeLabelStyleName(String styleName) {
        titleLinkLbl.removeStyleName(styleName);
    }

    public void addControl(Component control) {
        buttonControls.addComponent(control);
    }
}
