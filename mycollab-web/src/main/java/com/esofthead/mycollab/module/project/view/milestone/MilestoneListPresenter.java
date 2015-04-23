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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.module.project.view.ProjectGenericListPresenter;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class MilestoneListPresenter extends
		ProjectGenericListPresenter<MilestoneListView, MilestoneSearchCriteria, SimpleMilestone>
		implements ListCommand<MilestoneSearchCriteria> {
	private static final long serialVersionUID = 1L;

	private final MilestoneService milestoneService;

	public MilestoneListPresenter() {
		super(MilestoneListView.class, MilestoneListNoItemView.class);

		milestoneService = ApplicationContextUtil.getSpringBean(MilestoneService.class);
	}

	@Override
	protected void postInitView() {
		// Override to prevent setting up search handlers
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (CurrentProjectVariables
				.canRead(ProjectRolePermissionCollections.MILESTONES)) {
			MilestoneContainer milestoneContainer = (MilestoneContainer) container;
			milestoneContainer.navigateToContainer(ProjectTypeConstants.MILESTONE);
			milestoneContainer.removeAllComponents();
			milestoneContainer.addComponent(view.getWidget());

			MilestoneSearchCriteria searchCriteria;

			if (data.getParams() == null
					|| !(data.getParams() instanceof MilestoneSearchCriteria)) {
				searchCriteria = new MilestoneSearchCriteria();
				searchCriteria.setProjectId(new NumberSearchField(SearchField.AND,
						CurrentProjectVariables.getProjectId()));
			} else {
				searchCriteria = (MilestoneSearchCriteria) data.getParams();
			}

			int totalCount = milestoneService.getTotalCount(searchCriteria);

			if (totalCount > 0) {
				displayListView(container, data);
				doSearch(searchCriteria);
			} else {
				displayNoExistItems(container, data);
			}

			ProjectBreadcrumb breadcrumb = ViewManager
					.getCacheComponent(ProjectBreadcrumb.class);
			breadcrumb.gotoMilestoneList();
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doSearch(MilestoneSearchCriteria searchCriteria) {
		List<SimpleMilestone> milestones = milestoneService.findPagableListByCriteria(new SearchRequest<>(
				searchCriteria, 0, Integer.MAX_VALUE));
		view.displayMilestones(milestones);
	}

	@Override
	public ISearchableService<MilestoneSearchCriteria> getSearchService() {
		return milestoneService;
	}

	@Override
	protected void deleteSelectedItems() {
		throw new UnsupportedOperationException(
				"This presenter doesn't support this operation");
	}
}
