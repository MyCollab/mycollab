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
package com.mycollab.module.project.view;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.FollowingTicket;
import com.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.service.ProjectFollowingTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.parameters.BugScreenData;
import com.mycollab.module.project.view.parameters.ProjectScreenData;
import com.mycollab.module.project.view.parameters.RiskScreenData;
import com.mycollab.module.project.view.parameters.TaskScreenData;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.mvp.PageActionChain;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.UserLink;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;
import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class FollowingTicketTableDisplay extends DefaultPagedBeanTable<ProjectFollowingTicketService, FollowingTicketSearchCriteria, FollowingTicket> {
    private static final long serialVersionUID = 1L;

    public FollowingTicketTableDisplay() {
        super(AppContextUtil.getSpringBean(ProjectFollowingTicketService.class),
                FollowingTicket.class, Arrays.asList(FollowingTicketFieldDef.summary,
                        FollowingTicketFieldDef.project, FollowingTicketFieldDef.assignee, FollowingTicketFieldDef.createdDate));

        this.addGeneratedColumn("name", (source, itemId, columnId) -> {
            final FollowingTicket ticket = getBeanByIndex(itemId);
            final MButton ticketLink = new MButton(ticket.getName()).withStyleName(WebUIConstants.BUTTON_LINK);

            if (ProjectTypeConstants.BUG.equals(ticket.getType())) {
                ticketLink.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG));

                if (BugStatus.Verified.name().equals(ticket.getStatus())) {
                    ticketLink.addStyleName(WebUIConstants.LINK_COMPLETED);
                } else if (ticket.getDueDate() != null && ticket.getDueDate().before(DateTimeUtils.getCurrentDateWithoutMS())) {
                    ticketLink.addStyleName(WebUIConstants.LINK_OVERDUE);
                }

                ticketLink.addClickListener(clickEvent -> {
                    int projectId = ticket.getProjectId();
                    int bugId = ticket.getTypeId();
                    PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                            new BugScreenData.Read(bugId));
                    EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
                });
            } else if (ProjectTypeConstants.TASK.equals(ticket.getType())) {
                ticketLink.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK));

                if ("Closed".equals(ticket.getStatus())) {
                    ticketLink.addStyleName(WebUIConstants.LINK_COMPLETED);
                } else {
                    if ("Pending".equals(ticket.getStatus())) {
                        ticketLink.addStyleName(WebUIConstants.LINK_PENDING);
                    } else if (ticket.getDueDate() != null && ticket.getDueDate().before(new GregorianCalendar().getTime())) {
                        ticketLink.addStyleName(WebUIConstants.LINK_OVERDUE);
                    }
                }

                ticketLink.addClickListener(clickEvent -> {
                    int projectId = ticket.getProjectId();
                    int taskId = ticket.getTypeId();
                    PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new TaskScreenData.Read(taskId));
                    EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
                });
            } else if (ProjectTypeConstants.RISK.equals(ticket.getType())) {
                ticketLink.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.RISK));

                if ("Closed".equals(ticket.getStatus())) {
                    ticketLink.addStyleName(WebUIConstants.LINK_COMPLETED);
                } else {
                    if ("Pending".equals(ticket.getStatus())) {
                        ticketLink.addStyleName(WebUIConstants.LINK_PENDING);
                    } else if (ticket.getDueDate() != null && ticket.getDueDate().before(new GregorianCalendar().getTime())) {
                        ticketLink.addStyleName(WebUIConstants.LINK_OVERDUE);
                    }
                }

                ticketLink.addClickListener(new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        final int projectId = ticket.getProjectId();
                        final int riskId = ticket.getTypeId();
                        final PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                                new RiskScreenData.Read(riskId));
                        EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
                    }
                });
            }

            return ticketLink;
        });

        this.addGeneratedColumn("projectName", (source, itemId, columnId) -> {
            final FollowingTicket ticket = getBeanByIndex(itemId);
            Div projectLinkDiv = new Div().appendText(ProjectAssetsManager.getAsset(ProjectTypeConstants.PROJECT).getHtml() + " ")
                    .appendChild(new A(ProjectLinkBuilder.generateProjectFullLink(ticket.getProjectId()))
                            .appendText(ticket.getProjectName()));
            return new ELabel(projectLinkDiv.write(), ContentMode.HTML).withStyleName(UIConstants.LABEL_WORD_WRAP);
        });

        this.addGeneratedColumn("assignUser", (source, itemId, columnId) -> {
            FollowingTicket ticket = getBeanByIndex(itemId);
            return new UserLink(ticket.getAssignUser(), ticket.getAssignUserAvatarId(), ticket.getAssignUserFullName());
        });

        this.addGeneratedColumn("monitorDate", (source, itemId, columnId) -> {
            FollowingTicket ticket = FollowingTicketTableDisplay.this.getBeanByIndex(itemId);
            return new ELabel().prettyDateTime(ticket.getMonitorDate());
        });
    }
}
