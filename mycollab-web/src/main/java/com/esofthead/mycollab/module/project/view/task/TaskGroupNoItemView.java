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
/**
 * This file is part of mycollab-web.
 * <p>
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.ProjectListNoItemView;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

/**
 *
 * @author MyCollab Ltd.
 * @since 4.1.2
 *
 */

@ViewComponent
public class TaskGroupNoItemView extends ProjectListNoItemView {
    private static final long serialVersionUID = -3777541771073533444L;

    @Override
    protected FontAwesome viewIcon() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK_LIST);
    }

    @Override
    protected String viewTitle() {
        return AppContext.getMessage(TaskGroupI18nEnum.NO_ITEM_VIEW_TITLE);
    }

    @Override
    protected String viewHint() {
        return AppContext.getMessage(TaskGroupI18nEnum.NO_ITEM_VIEW_HINT);
    }

    @Override
    protected String actionMessage() {
        return AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASKGROUP);
    }

    @Override
    protected Button.ClickListener actionListener() {
        return new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                final TaskGroupAddWindow taskListWindow = new TaskGroupAddWindow(
                        null);
                UI.getCurrent().addWindow(taskListWindow);
            }
        };
    }

    @Override
    protected boolean hasPermission() {
        return CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS);
    }
}
