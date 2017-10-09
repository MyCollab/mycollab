package com.mycollab.vaadin.web.ui;

import com.mycollab.core.MyCollabException;
import com.mycollab.web.CustomLayoutExt;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public abstract class ReadViewLayout extends CustomLayoutExt {
    private static final long serialVersionUID = 1L;

    public ReadViewLayout() {
        super("readView");
    }

    public void addHeader(Component header) {
        this.addComponent(header, "readViewHeader");
    }

    public void addBody(Component body) {
        this.addComponent(body, "readViewBody");
    }

    public void addBottomControls(Component bottomControls) {
        this.addComponent(bottomControls, "readViewBottomControls");
    }

    public void addTitleStyleName(String styleName) {
        throw new MyCollabException("Must be implemented in the sub class");
    }

    public void removeTitleStyleName(String styleName) {
        throw new MyCollabException("Must be implemented in the sub class");
    }
}
