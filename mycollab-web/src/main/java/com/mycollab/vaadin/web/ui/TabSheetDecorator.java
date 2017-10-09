package com.mycollab.vaadin.web.ui;

import com.mycollab.vaadin.mvp.PageView;
import com.vaadin.server.Resource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class TabSheetDecorator extends TabSheet {
    private static final long serialVersionUID = 1L;

    public Tab selectTab(final String viewName) {
        int compCount = this.getComponentCount();
        for (int i = 0; i < compCount; i++) {
            Tab tab = this.getTab(i);
            if (tab.getCaption().equals(viewName)) {
                this.setSelectedTab(tab);
                return tab;
            }
        }

        return null;
    }

    public Tab getSelectedTabInfo() {
        return this.getTab(this.getSelectedTab());
    }

    public void addWrappedTab(String tabName, Resource icon) {
        addTab(new WrappedTab(), tabName, icon);
    }

    public static class WrappedTab extends CssLayout {
        public WrappedTab() {
            setSizeFull();
        }

        public void addView(PageView view) {
            removeAllComponents();
            addComponent(view);
        }
    }
}
