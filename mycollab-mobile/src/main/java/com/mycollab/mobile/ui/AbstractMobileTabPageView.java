package com.mycollab.mobile.ui;

import com.mycollab.vaadin.mvp.PageView;
import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.Tab;

/**
 * @author MyCollab Ltd.
 * @since 4.2
 */
public class AbstractMobileTabPageView extends TabBarView implements PageView {
    private static final long serialVersionUID = 664039475002291943L;

    /*
     * Now we use EventBus to manage event
     * @see com.mycollab.vaadin.EventBusFactory
     */
    @Deprecated
    @Override
    public void addViewListener(ViewListener listener) {
        // Do nothing
    }

    @Override
    public Tab addTab(Component tabContent, String caption, Resource icon) {
        Tab newTab = super.addTab(tabContent, caption, icon);
        ((Button) newTab).setHtmlContentAllowed(true);
        return newTab;
    }
}
