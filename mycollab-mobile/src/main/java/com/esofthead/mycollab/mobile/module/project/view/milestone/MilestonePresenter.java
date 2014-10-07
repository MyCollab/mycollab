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
package com.esofthead.mycollab.mobile.module.project.view.milestone;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.mobile.module.project.ui.InsideProjectNavigationMenu;
import com.esofthead.mycollab.mobile.module.project.view.parameters.MilestoneScreenData;
import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class MilestonePresenter extends AbstractPresenter<MilestoneContainer> {

	private static final long serialVersionUID = 5263058263047835714L;

	public MilestonePresenter() {
		super(MilestoneContainer.class);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		InsideProjectNavigationMenu projectModuleMenu = (InsideProjectNavigationMenu) ((MobileNavigationManager) UI
				.getCurrent().getContent()).getNavigationMenu();
		projectModuleMenu.selectButton(AppContext
				.getMessage(ProjectCommonI18nEnum.VIEW_MILESTONE));

		AbstractPresenter<?> presenter = null;

		if (data instanceof MilestoneScreenData.List) {
			presenter = PresenterResolver
					.getPresenter(MilestoneListPresenter.class);
		} else if (data instanceof MilestoneScreenData.Add
				|| data instanceof MilestoneScreenData.Edit) {
			presenter = PresenterResolver
					.getPresenter(MilestoneAddPresenter.class);
		} else if (data instanceof MilestoneScreenData.Read) {
			presenter = PresenterResolver
					.getPresenter(MilestoneReadPresenter.class);
		} else {
			throw new MyCollabException("Do not support screen data " + data);
		}

		presenter.go(container, data);
	}

}
