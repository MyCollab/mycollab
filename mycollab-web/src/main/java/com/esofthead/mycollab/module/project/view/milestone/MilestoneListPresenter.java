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

import java.util.List;

import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.mvp.ListCommand;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MilestoneListPresenter extends
		AbstractPresenter<MilestoneListView> implements
		ListCommand<MilestoneSearchCriteria> {
	private static final long serialVersionUID = 1L;

	public MilestoneListPresenter() {
		super(MilestoneListView.class);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.MILESTONES)) {
			MilestoneContainer milestoneContainer = (MilestoneContainer) container;
			milestoneContainer.removeAllComponents();
			milestoneContainer.addComponent(view.getWidget());
			doSearch((MilestoneSearchCriteria) data.getParams());

			ProjectBreadcrumb breadcrumb = ViewManager
					.getView(ProjectBreadcrumb.class);
			breadcrumb.gotoMilestoneList();
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	@Override
	public void doSearch(MilestoneSearchCriteria searchCriteria) {
		MilestoneService milestoneService = ApplicationContextUtil
				.getSpringBean(MilestoneService.class);
		List<SimpleMilestone> milestones = milestoneService
				.findPagableListByCriteria(new SearchRequest<MilestoneSearchCriteria>(
						searchCriteria, 0, Integer.MAX_VALUE));
		view.displayMilestones(milestones);
	}

}
