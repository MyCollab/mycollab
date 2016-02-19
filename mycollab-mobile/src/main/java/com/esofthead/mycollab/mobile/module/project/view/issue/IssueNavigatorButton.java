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
package com.esofthead.mycollab.mobile.module.project.view.issue;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.TicketI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class IssueNavigatorButton extends NavigationButton {
    private Integer milestoneId;

    public IssueNavigatorButton() {
        super(AppContext.getMessage(TicketI18nEnum.M_TICKET_NUM, 0));
        this.addClickListener(new NavigationButtonClickListener() {
            @Override
            public void buttonClick(NavigationButtonClickEvent event) {
                if (milestoneId != null) {
                    getNavigationManager().navigateTo(new IssueListView(milestoneId));
                }
            }
        });
    }

    public void displayTotalIssues(Integer milestoneId) {
        this.milestoneId = milestoneId;
        ProjectGenericTaskSearchCriteria criteria = new ProjectGenericTaskSearchCriteria();
        criteria.setMilestoneId(NumberSearchField.and(milestoneId));
        criteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK,
                ProjectTypeConstants.RISK));
        ProjectGenericTaskService ticketService = ApplicationContextUtil.getSpringBean(ProjectGenericTaskService.class);
        this.setCaption(AppContext.getMessage(TicketI18nEnum.M_TICKET_NUM, ticketService.getTotalCount(criteria)));
    }
}
