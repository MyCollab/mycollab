/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
