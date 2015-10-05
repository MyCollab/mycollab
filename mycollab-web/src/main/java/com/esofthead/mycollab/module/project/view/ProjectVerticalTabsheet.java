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
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ProjectVerticalTabsheet extends VerticalTabsheet {
    private static final long serialVersionUID = 1L;

    private Button toogleBtn;

    @Override
    protected void setDefaulButtonIcon(Component btn, Boolean selected) {
        ButtonTabImpl btnTabImpl = (ButtonTabImpl) btn;
        String tabId = btnTabImpl.getTabId();

        Resource resource = ProjectAssetsManager.getAsset(tabId);
        btn.setIcon(resource);
    }

    @Override
    public void setNavigatorVisibility(boolean visibility) {
        if (!visibility) {
            navigatorWrapper.setWidth("70px");
            navigatorContainer.setWidth("70px");
            this.hideTabsCaption();

            toogleBtn.setIcon(FontAwesome.CARET_SQUARE_O_RIGHT);
            toogleBtn.setDescription("Expand menu");
            toogleBtn.setCaption("");
        } else {
            navigatorWrapper.setWidth("220px");
            navigatorContainer.setWidth("220px");
            this.showTabsCaption();

            toogleBtn.setIcon(FontAwesome.CARET_SQUARE_O_LEFT);
            toogleBtn.setDescription("");
            toogleBtn.setCaption("Collapse menu");
        }

        CurrentProjectVariables.setProjectToogleMenu(visibility);
    }

    public void addToogleNavigatorControl() {
        toogleBtn = this.addButtonOnNavigatorContainer("button", "Collapse menu", FontAwesome.CARET_SQUARE_O_LEFT);
        toogleBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean visibility = CurrentProjectVariables.getProjectToogleMenu();
                setNavigatorVisibility(!visibility);
            }
        });

        boolean visibility = CurrentProjectVariables.getProjectToogleMenu();
        setNavigatorVisibility(visibility);
    }
}
