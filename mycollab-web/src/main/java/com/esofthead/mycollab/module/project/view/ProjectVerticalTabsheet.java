/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.vaadin.web.ui.VerticalTabsheet;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ProjectVerticalTabsheet extends VerticalTabsheet {
    private static final long serialVersionUID = 1L;

    private Button toogleBtn;

    @Override
    protected void setDefaultButtonIcon(Component btn, Boolean selected) {
        ButtonTabImpl btnTabImpl = (ButtonTabImpl) btn;
        String tabId = btnTabImpl.getTabId();

        Resource resource = ProjectAssetsManager.getAsset(tabId);
        btn.setIcon(resource);
    }

    @Override
    public void setNavigatorVisibility(boolean visibility) {
        if (!visibility) {
            navigatorWrapper.setWidth("60px");
            navigatorContainer.setWidth("60px");
            this.hideTabsCaption();

            toogleBtn.setIcon(FontAwesome.CARET_SQUARE_O_RIGHT);
            toogleBtn.setDescription("Expand menu");
            toogleBtn.setCaption("");
        } else {
            navigatorWrapper.setWidth("200px");
            navigatorContainer.setWidth("200px");
            this.showTabsCaption();

            toogleBtn.setIcon(FontAwesome.CARET_SQUARE_O_LEFT);
            toogleBtn.setDescription("");
            toogleBtn.setCaption("Collapse menu");
        }

        CurrentProjectVariables.setProjectToggleMenu(visibility);
    }

    public void addToggleNavigatorControl() {
        Button btn = this.addButtonOnNavigatorContainer("button", "Collapse menu", FontAwesome.CARET_SQUARE_O_LEFT);
        if (btn != null) {
            toogleBtn = btn;
            toogleBtn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    boolean visibility = CurrentProjectVariables.getProjectToggleMenu();
                    setNavigatorVisibility(!visibility);
                }
            });
        }

        boolean visibility = CurrentProjectVariables.getProjectToggleMenu();
        setNavigatorVisibility(visibility);
    }
}
