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
package com.esofthead.mycollab.module.project.view.page;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.PageEvent;
import com.esofthead.mycollab.module.project.i18n.Page18InEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.ProjectListNoItemView;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.2
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class PageListNoItemView extends ProjectListNoItemView {
    @Override
    protected FontAwesome viewIcon() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.PAGE);
    }

    @Override
    protected String viewTitle() {
        return AppContext.getMessage(Page18InEnum.VIEW_NO_ITEM_TITLE);
    }

    @Override
    protected String viewHint() {
        return AppContext.getMessage(Page18InEnum.VIEW_NO_ITEM_HINT);
    }

    @Override
    protected String actionMessage() {
        return AppContext.getMessage(Page18InEnum.BUTTON_NEW_PAGE);
    }

    @Override
    protected Button.ClickListener actionListener() {
        return new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(
                        new PageEvent.GotoAdd(this, null));
            }
        };
    }

    protected MHorizontalLayout createControlButtons() {
        Button createPageBtn = new Button(actionMessage(), actionListener());
        createPageBtn.setEnabled(hasPermission());
        createPageBtn.addStyleName(UIConstants.THEME_GREEN_LINK);

        Button createPageGroupBtn = new Button(AppContext.getMessage(Page18InEnum.BUTTON_NEW_GROUP), new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().addWindow(new GroupPageAddWindow());
            }
        });

        MHorizontalLayout links = new MHorizontalLayout();
        links.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        links.with(createPageBtn, new Label(" or "), createPageGroupBtn);
        return links;
    }

    @Override
    protected boolean hasPermission() {
        return CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.PAGES);
    }
}