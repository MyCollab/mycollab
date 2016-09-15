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
package com.mycollab.module.project.view;

import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.VerticalTabsheet;
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

    private Button toggleBtn;

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
            navigatorWrapper.setWidth("70px");
            navigatorContainer.setWidth("70px");
            this.hideTabsCaption();

            toggleBtn.setIcon(FontAwesome.CARET_SQUARE_O_RIGHT);
            toggleBtn.setDescription(UserUIContext.getMessage(ProjectI18nEnum.ACTION_EXPAND_MENU));
            toggleBtn.setCaption("");
        } else {
            navigatorWrapper.setWidth("200px");
            navigatorContainer.setWidth("200px");
            this.showTabsCaption();

            toggleBtn.setIcon(FontAwesome.CARET_SQUARE_O_LEFT);
            toggleBtn.setDescription("");
            toggleBtn.setCaption(UserUIContext.getMessage(ProjectI18nEnum.ACTION_COLLAPSE_MENU));
        }

        CurrentProjectVariables.setProjectToggleMenu(visibility);
    }

    public void addToggleNavigatorControl() {
        Button btn = this.addButtonOnNavigatorContainer("button", UserUIContext.getMessage(ProjectI18nEnum.ACTION_COLLAPSE_MENU),
                FontAwesome.CARET_SQUARE_O_LEFT);
        if (btn != null) {
            toggleBtn = btn;
            toggleBtn.addClickListener(clickEvent -> {
                boolean visibility = CurrentProjectVariables.getProjectToggleMenu();
                setNavigatorVisibility(!visibility);
            });
        }

        boolean visibility = CurrentProjectVariables.getProjectToggleMenu();
        setNavigatorVisibility(visibility);
    }
}
