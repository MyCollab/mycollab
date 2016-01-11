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

import com.esofthead.mycollab.common.i18n.DayI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePageView;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TicketI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.hp.gagawa.java.elements.A;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class IssueListView extends AbstractMobilePageView {
    private Integer milestoneId;
    private final BeanList<ProjectGenericTaskService, ProjectGenericTaskSearchCriteria, ProjectGenericTask> ticketList;

    public IssueListView(Integer milestoneId) {
        this.milestoneId = milestoneId;
        ticketList = new BeanList<>(ApplicationContextUtil.getSpringBean(ProjectGenericTaskService.class), TicketRowDisplayHandler.class);
        ticketList.setDisplayEmptyListText(false);
        this.setContent(ticketList);
        displayTickets();
    }

    private void displayTickets() {
        ProjectGenericTaskSearchCriteria criteria = new ProjectGenericTaskSearchCriteria();
        criteria.setMilestoneId(NumberSearchField.and(milestoneId));
        criteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK));
        int numTickets = ticketList.setSearchCriteria(criteria);
        this.setCaption(AppContext.getMessage(TicketI18nEnum.M_TICKET_NUM, numTickets));
    }

    public static class TicketRowDisplayHandler extends BeanList.RowDisplayHandler<ProjectGenericTask> {
        private static final long serialVersionUID = 7604097872938029830L;

        @Override
        public Component generateRow(ProjectGenericTask issue, int rowIndex) {
            MVerticalLayout ticketLayout = new MVerticalLayout().withWidth("100%");
            A issueLink;
            if (ProjectTypeConstants.BUG.equals(issue.getType())) {
                issueLink = new A(ProjectLinkBuilder.generateBugPreviewFullLink(issue.getExtraTypeId(), issue.getProjectShortName()))
                        .appendText(String.format("[#%s] - %s", issue.getExtraTypeId(), issue.getName()));
            } else if (ProjectTypeConstants.TASK.equals(issue.getType())) {
                issueLink = new A(ProjectLinkBuilder.generateTaskPreviewFullLink(issue.getExtraTypeId(), issue.getProjectShortName()))
                        .appendText(String.format("[#%s] - %s", issue.getExtraTypeId(), issue.getName()));
            } else {
                throw new MyCollabException("Do not support issue type " + issue.getType());
            }
            CssLayout taskLbl = new CssLayout(new ELabel(issueLink.write(), ContentMode.HTML).withStyleName(UIConstants.TRUNCATE));
            ticketLayout.with(new MHorizontalLayout(new ELabel(ProjectAssetsManager.getAsset(issue.getType())
                    .getHtml(), ContentMode.HTML).withWidthUndefined(), taskLbl).expand(taskLbl).withFullWidth());

            CssLayout metaInfoLayout = new CssLayout();
            ticketLayout.with(metaInfoLayout);

            ELabel lastUpdatedTimeLbl = new ELabel(AppContext.getMessage(DayI18nEnum.LAST_UPDATED_ON, AppContext
                    .formatPrettyTime((issue.getLastUpdatedTime())))).withStyleName(UIConstants.META_INFO);
            metaInfoLayout.addComponent(lastUpdatedTimeLbl);

            A assigneeLink = new A();
            assigneeLink.setHref(ProjectLinkGenerator.generateProjectMemberFullLink(AppContext.getSiteUrl(),
                    CurrentProjectVariables.getProjectId(), issue.getAssignUser()));
            assigneeLink.appendText(issue.getAssignUserFullName());

            ELabel assigneeLbl = new ELabel(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE) + (issue
                    .getAssignUserFullName() == null ?
                    ":&nbsp;N/A&nbsp;" : ":&nbsp;" + assigneeLink.write()), ContentMode.HTML).withStyleName(UIConstants.META_INFO);
            assigneeLbl.addStyleName(UIConstants.TRUNCATE);
            metaInfoLayout.addComponent(assigneeLbl);

            ELabel statusLbl = new ELabel(AppContext.getMessage(TaskI18nEnum.FORM_STATUS) + ": " + AppContext.getMessage
                    (OptionI18nEnum.BugStatus.class, issue.getStatus()), ContentMode.HTML).withStyleName(UIConstants
                    .META_INFO);
            metaInfoLayout.addComponent(statusLbl);

            return ticketLayout;
        }
    }
}
