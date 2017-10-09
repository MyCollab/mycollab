package com.mycollab.module.project.view;

import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class AbstractProjectPageView extends AbstractVerticalPageView {
    private static final long serialVersionUID = 1L;

    protected ELabel headerText;
    protected MCssLayout contentWrapper;
    protected MHorizontalLayout header;

    public AbstractProjectPageView(String headerText, FontAwesome icon) {
        this.headerText = ELabel.h2(icon.getHtml() + " " + headerText);
        super.addComponent(constructHeader());

        contentWrapper = new MCssLayout().withStyleName(WebThemes.CONTENT_WRAPPER);
        super.addComponent(contentWrapper);

    }

    private ComponentContainer constructHeader() {
        header = new MHorizontalLayout().with(headerText).withStyleName("hdr-view").withFullWidth().withMargin(true);
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        return header;
    }

    public void addHeaderRightContent(Component c) {
        header.with(c).withAlign(c, Alignment.MIDDLE_RIGHT);
    }

    @Override
    public void addComponent(Component c) {
        contentWrapper.addComponent(c);
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        contentWrapper.replaceComponent(oldComponent, newComponent);
    }

    public ComponentContainer getBody() {
        return contentWrapper;
    }
}
