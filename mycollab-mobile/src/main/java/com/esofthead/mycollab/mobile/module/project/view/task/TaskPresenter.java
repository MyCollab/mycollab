/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.task;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.InsideProjectNavigationMenu;
import com.esofthead.mycollab.mobile.module.project.view.parameters.TaskGroupScreenData;
import com.esofthead.mycollab.mobile.module.project.view.parameters.TaskScreenData;
import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 *
 */
public class TaskPresenter extends AbstractPresenter<TaskContainer> {

	private static final long serialVersionUID = 7999611450505328038L;

	public TaskPresenter() {
		super(TaskContainer.class);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.TASKS)) {
			InsideProjectNavigationMenu projectModuleMenu = (InsideProjectNavigationMenu) ((MobileNavigationManager) UI
					.getCurrent().getContent()).getNavigationMenu();
			projectModuleMenu.selectButton(AppContext
					.getMessage(ProjectCommonI18nEnum.VIEW_TASK));
			AbstractPresenter<?> presenter = null;

			if (data instanceof TaskGroupScreenData.List || data == null) {
				presenter = PresenterResolver
						.getPresenter(TaskGroupListPresenter.class);
			} else if (data instanceof TaskScreenData.List) {
				presenter = PresenterResolver
						.getPresenter(TaskListPresenter.class);
			} else if (data instanceof TaskScreenData.Read) {
				presenter = PresenterResolver
						.getPresenter(TaskReadPresenter.class);
			} else if (data instanceof TaskScreenData.Add
					|| data instanceof TaskScreenData.Edit) {
				presenter = PresenterResolver
						.getPresenter(TaskAddPresenter.class);
			} else if (data instanceof TaskGroupScreenData.Read) {
				presenter = PresenterResolver
						.getPresenter(TaskGroupReadPresenter.class);
			} else if (data instanceof TaskGroupScreenData.Add
					|| data instanceof TaskGroupScreenData.Edit) {
				presenter = PresenterResolver
						.getPresenter(TaskGroupAddPresenter.class);
			} else {
				throw new MyCollabException("Do not support param: " + data);
			}

			presenter.go(container, data);
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

}
