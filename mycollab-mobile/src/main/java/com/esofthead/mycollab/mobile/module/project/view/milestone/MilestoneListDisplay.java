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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */
public class MilestoneListDisplay
		extends
		DefaultPagedBeanList<MilestoneService, MilestoneSearchCriteria, SimpleMilestone> {

	private static final long serialVersionUID = 253054104668116456L;

	public MilestoneListDisplay() {
		super(ApplicationContextUtil.getSpringBean(MilestoneService.class),
				new MilestoneRowDisplayHandler());
		this.setDisplayNumItems(Integer.MAX_VALUE);
	}

	private static class MilestoneRowDisplayHandler implements
			RowDisplayHandler<SimpleMilestone> {

		@Override
		public Component generateRow(SimpleMilestone milestone, int rowIndex) {
			HorizontalLayout layout = new HorizontalLayout();
			layout.setStyleName("list-item");
			layout.setSpacing(true);
			Label milestoneIconLbl = new Label(
					"<span aria-hidden=\"true\" data-icon=\""
							+ IconConstants.PROJECT_MILESTONE + "\"></span>");
			milestoneIconLbl.setStyleName("milestone-icon");
			milestoneIconLbl.setWidthUndefined();
			milestoneIconLbl.setContentMode(ContentMode.HTML);
			layout.addComponent(milestoneIconLbl);

			VerticalLayout milestoneInfoLayout = new VerticalLayout();
			milestoneInfoLayout.setWidth("100%");

			Button milestoneNameBtn = new Button(milestone.getName());
			milestoneNameBtn.setStyleName("milestone-name");
			milestoneInfoLayout.addComponent(milestoneNameBtn);

			Label milestoneDatesInfo = new Label();
			milestoneDatesInfo.setValue(AppContext.getMessage(
					MilestoneI18nEnum.M_LIST_DATE_INFO,
					AppContext.formatDate(milestone.getStartdate()),
					AppContext.formatDate(milestone.getEnddate())));
			milestoneDatesInfo.setStyleName("milestone-meta-info");
			milestoneInfoLayout.addComponent(milestoneDatesInfo);

			Label assigneeLbl = new Label();
			assigneeLbl.setValue(AppContext
					.getMessage(GenericI18Enum.FORM_ASSIGNEE)
					+ ":&nbsp;<span class='milestone-assignee'>"
					+ milestone.getOwnerFullName() + "</span>");
			assigneeLbl.setStyleName("milestone-meta-info");
			assigneeLbl.setContentMode(ContentMode.HTML);
			milestoneInfoLayout.addComponent(assigneeLbl);

			Label taskBugLbl = new Label();
			taskBugLbl.setValue(AppContext.getMessage(
					MilestoneI18nEnum.M_LIST_TASK_BUG_INFO,
					"<span aria-hidden=\"true\" data-icon=\""
							+ IconConstants.PROJECT_TASK + "\"></span>",
					milestone.getNumOpenTasks(), milestone.getNumTasks(),
					"<span aria-hidden=\"true\" data-icon=\""
							+ IconConstants.PROJECT_BUG + "\"></span>",
					milestone.getNumOpenBugs(), milestone.getNumOpenBugs()));
			taskBugLbl.setStyleName("milestone-meta-info");
			taskBugLbl.setContentMode(ContentMode.HTML);
			milestoneInfoLayout.addComponent(taskBugLbl);

			layout.addComponent(milestoneInfoLayout);
			layout.setExpandRatio(milestoneInfoLayout, 1.0f);
			layout.setWidth("100%");
			return layout;
		}

	}
}
