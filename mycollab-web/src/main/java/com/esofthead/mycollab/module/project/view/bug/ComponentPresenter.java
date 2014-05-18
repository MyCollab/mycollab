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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.localization.BugI18nEnum;
import com.esofthead.mycollab.module.project.view.parameters.ComponentScreenData;
import com.esofthead.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ComponentPresenter
		extends
		AbstractPresenter<com.esofthead.mycollab.module.project.view.bug.ComponentContainer> {
	private static final long serialVersionUID = 1L;

	public ComponentPresenter() {
		super(
				com.esofthead.mycollab.module.project.view.bug.ComponentContainer.class);
	}

	@Override
	public void go(ComponentContainer container, ScreenData<?> data) {
		super.go(container, data, false);
	}

	@Override
	protected void onGo(com.vaadin.ui.ComponentContainer container,
			ScreenData<?> data) {
		TrackerContainer trackerContainer = (TrackerContainer) container;
		trackerContainer.gotoSubView(AppContext
				.getMessage(BugI18nEnum.COMPONENT_TAB));

		view.removeAllComponents();

		AbstractPresenter<?> presenter = null;

		if (data instanceof ComponentScreenData.Add) {
			presenter = PresenterResolver
					.getPresenter(ComponentAddPresenter.class);
		} else if (data instanceof ComponentScreenData.Edit) {
			presenter = PresenterResolver
					.getPresenter(ComponentAddPresenter.class);
		} else if (data instanceof ComponentScreenData.Search) {
			presenter = PresenterResolver
					.getPresenter(ComponentListPresenter.class);
		} else if (data instanceof ComponentScreenData.Read) {
			presenter = PresenterResolver
					.getPresenter(ComponentReadPresenter.class);
		} else if (data == null) {
			ComponentSearchCriteria criteria = new ComponentSearchCriteria();
			criteria.setProjectid(new NumberSearchField(CurrentProjectVariables
					.getProjectId()));
			data = new ComponentScreenData.Search(criteria);
			presenter = PresenterResolver
					.getPresenter(ComponentListPresenter.class);
		} else {
			throw new MyCollabException("Do not support screen data");
		}

		presenter.go(view, data);
	}

}
