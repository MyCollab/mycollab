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
package com.mycollab.mobile.module.project.view.issue;

import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.DayI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.MyCollabException;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.BeanList;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
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
    private final BeanList<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> ticketList;

    public IssueListView(Integer milestoneId) {
        this.milestoneId = milestoneId;
        ticketList = new BeanList<>(AppContextUtil.getSpringBean(ProjectTicketService.class), TicketRowDisplayHandler.class);
        ticketList.setDisplayEmptyListText(false);
        this.setContent(ticketList);
        displayTickets();
    }

    private void displayTickets() {
        ProjectTicketSearchCriteria criteria = new ProjectTicketSearchCriteria();
        criteria.setMilestoneId(NumberSearchField.equal(milestoneId));
        criteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK,
                ProjectTypeConstants.RISK));
        int numTickets = ticketList.setSearchCriteria(criteria);
        this.setCaption(UserUIContext.getMessage(TicketI18nEnum.OPT_TICKETS_VALUE, numTickets));
    }

    public static class TicketRowDisplayHandler extends BeanList.RowDisplayHandler<ProjectTicket> {
        private static final long serialVersionUID = 7604097872938029830L;

        @Override
        public Component generateRow(ProjectTicket issue, int rowIndex) {
            MVerticalLayout ticketLayout = new MVerticalLayout().withFullWidth().withStyleName("row");
            A issueLink;
            if (ProjectTypeConstants.BUG.equals(issue.getType())) {
                issueLink = new A(ProjectLinkBuilder.generateBugPreviewFullLink(issue.getExtraTypeId(), issue.getProjectShortName()))
                        .appendText(String.format("[#%s] - %s", issue.getExtraTypeId(), issue.getName()));
            } else if (ProjectTypeConstants.TASK.equals(issue.getType())) {
                issueLink = new A(ProjectLinkBuilder.generateTaskPreviewFullLink(issue.getExtraTypeId(), issue.getProjectShortName()))
                        .appendText(String.format("[#%s] - %s", issue.getExtraTypeId(), issue.getName()));
            } else if (ProjectTypeConstants.RISK.equals(issue.getType())) {
                issueLink = new A(ProjectLinkBuilder.generateRiskPreviewFullLink(issue.getProjectId(), issue.getTypeId()))
                        .appendText(issue.getName());
            } else {
                throw new MyCollabException("Do not support issue type " + issue.getType());
            }
            ELabel taskLbl = ELabel.html(issueLink.write()).withStyleName(UIConstants.TEXT_ELLIPSIS);
            ticketLayout.with(new MHorizontalLayout(ELabel.fontIcon(ProjectAssetsManager.getAsset(issue.getType())).withWidthUndefined(),
                    taskLbl).expand(taskLbl).withFullWidth());

            CssLayout metaInfoLayout = new CssLayout();
            ticketLayout.with(metaInfoLayout);

            ELabel lastUpdatedTimeLbl = new ELabel(UserUIContext.getMessage(DayI18nEnum.LAST_UPDATED_ON, UserUIContext
                    .formatPrettyTime((issue.getLastUpdatedTime())))).withStyleName(UIConstants.META_INFO);
            metaInfoLayout.addComponent(lastUpdatedTimeLbl);

            A assigneeLink = new A();
            assigneeLink.setHref(ProjectLinkGenerator.generateProjectMemberFullLink(MyCollabUI.getSiteUrl(),
                    CurrentProjectVariables.getProjectId(), issue.getAssignUser()));
            assigneeLink.appendText(issue.getAssignUserFullName());

            ELabel assigneeLbl = new ELabel(UserUIContext.getMessage(GenericI18Enum.FORM_ASSIGNEE) + (issue.getAssignUserFullName() == null ?
                    ":&nbsp;N/A&nbsp;" : ":&nbsp;" + assigneeLink.write()), ContentMode.HTML).withStyleName(UIConstants.META_INFO);
            assigneeLbl.addStyleName(UIConstants.TEXT_ELLIPSIS);
            metaInfoLayout.addComponent(assigneeLbl);

            ELabel statusLbl = new ELabel(UserUIContext.getMessage(GenericI18Enum.FORM_STATUS) + ": " + UserUIContext.getMessage
                    (OptionI18nEnum.BugStatus.class, issue.getStatus()), ContentMode.HTML).withStyleName(UIConstants.META_INFO);
            metaInfoLayout.addComponent(statusLbl);

            return ticketLayout;
        }
    }
}
