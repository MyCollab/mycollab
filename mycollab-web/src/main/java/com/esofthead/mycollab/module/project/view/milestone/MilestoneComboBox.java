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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.MilestoneStatusConstant;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComboBox;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class MilestoneComboBox extends ComboBox {

	public MilestoneComboBox() {
		super();
		this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);

		MilestoneSearchCriteria criteria = new MilestoneSearchCriteria();
		SimpleProject project = CurrentProjectVariables.getProject();
		if (project != null) {
			criteria.setProjectId(new NumberSearchField(SearchField.AND,
					project.getId()));

			MilestoneService milestoneService = ApplicationContextUtil
					.getSpringBean(MilestoneService.class);
			List<SimpleMilestone> milestoneList = (List<SimpleMilestone>) milestoneService
					.findPagableListByCriteria(new SearchRequest<MilestoneSearchCriteria>(
							criteria, 0, Integer.MAX_VALUE));

			Collections.sort(milestoneList, new MilestoneComparator());

			for (SimpleMilestone milestone : milestoneList) {
				this.addItem(milestone.getId());
				this.setItemCaption(milestone.getId(), milestone.getName());

				Resource iconRes = null;
				if (MilestoneStatusConstant.IN_PROGRESS.equals(milestone
						.getStatus())) {
					iconRes = MyCollabResource
							.newResource("icons/16/project/phase_progress.png");
				} else if (MilestoneStatusConstant.FUTURE.equals(milestone
						.getStatus())) {
					iconRes = MyCollabResource
							.newResource("icons/16/project/phase_future.png");
				} else {
					iconRes = MyCollabResource
							.newResource("icons/16/project/phase_closed.png");
				}

				this.setItemIcon(milestone.getId(), iconRes);
			}
		}

	}

	private static class MilestoneComparator implements Comparator<Milestone> {

		@Override
		public int compare(Milestone milestone1, Milestone milestone2) {
			if (MilestoneStatusConstant.IN_PROGRESS.equals(milestone1
					.getStatus())) {
				return -1;
			} else if (MilestoneStatusConstant.FUTURE.equals(milestone1
					.getStatus())) {
				return MilestoneStatusConstant.IN_PROGRESS.equals(milestone2
						.getStatus()) ? 1 : -1;
			} else {
				return 1;
			}
		}

	}
}
