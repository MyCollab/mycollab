package com.mycollab.vaadin.web.ui;

import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
public class DefaultReadViewLayout extends ReadViewLayout {
    private ELabel titleLbl;

    public DefaultReadViewLayout(String title) {
        super();
        this.setWidth("100%");
        this.addHeader(buildHeader(title));
    }

    private ComponentContainer buildHeader(String title) {
        MHorizontalLayout header = new MHorizontalLayout().withFullWidth();
        titleLbl = ELabel.h3(title);
        header.with(titleLbl).expand(titleLbl);
        return header;
    }

    @Override
    public void addTitleStyleName(final String styleName) {
        titleLbl.addStyleName(styleName);
    }

    @Override
    public void removeTitleStyleName(final String styleName) {
        titleLbl.removeStyleName(styleName);
    }

    public void setTitle(final String title) {
        titleLbl.setValue(title);
    }
}
