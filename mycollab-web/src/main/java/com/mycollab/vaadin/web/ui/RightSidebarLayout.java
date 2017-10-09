/**
 *
 */
package com.mycollab.vaadin.web.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.4
 */
public class RightSidebarLayout extends CssLayout {
    private static final long serialVersionUID = 6058720774092113093L;

    private final CssLayout contentWrap;
    private final CssLayout sidebarWrap;

    public RightSidebarLayout() {
        this.setStyleName("rightsidebar-layout");
        this.setWidth("100%");

        contentWrap = new CssLayout();
        contentWrap.setStyleName("content-wrap");
        contentWrap.setWidth("100%");
        this.addComponent(contentWrap);

        sidebarWrap = new CssLayout();
        sidebarWrap.setStyleName("sidebar-wrap");
        sidebarWrap.setWidth("250px");
        this.addComponent(sidebarWrap);
    }

    public void setContent(Component c) {
        contentWrap.removeAllComponents();
        contentWrap.addComponent(c);
    }

    public void setSidebar(Component c) {
        sidebarWrap.removeAllComponents();
        sidebarWrap.addComponent(c);
    }

}
