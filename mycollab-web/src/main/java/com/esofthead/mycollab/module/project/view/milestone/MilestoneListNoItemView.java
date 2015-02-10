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
package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.ProjectListNoItemView;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */

@ViewComponent(scope = ViewScope.PROTOTYPE)
public class MilestoneListNoItemView extends ProjectListNoItemView {
	private static final long serialVersionUID = 740826537581761743L;

    @Override
    protected FontAwesome viewIcon() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE);
    }

    @Override
    protected String viewTitle() {
        return AppContext.getMessage(MilestoneI18nEnum.VIEW_NO_ITEM_TITLE);
    }

    @Override
    protected String viewHint() {
        return AppContext.getMessage(MilestoneI18nEnum.VIEW_NO_ITEM_HINT);
    }

    @Override
    protected String actionMessage() {
        return AppContext.getMessage(MilestoneI18nEnum.BUTTON_NEW_PHASE);
    }

    @Override
    protected Button.ClickListener actionListener() {
        return new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(
                        new MilestoneEvent.GotoAdd(this, null));
            }
        };
    }

    @Override
    protected boolean hasPermission() {
        return CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES);
    }

}
