/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view;

import com.mycollab.module.project.domain.FollowingTicket;
import com.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria;
import com.mycollab.module.project.service.ProjectFollowingTicketService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.table.DefaultPagedGrid;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
public class FollowingTicketTableDisplay extends DefaultPagedGrid<ProjectFollowingTicketService, FollowingTicketSearchCriteria, FollowingTicket> {
    private static final long serialVersionUID = 1L;

    public FollowingTicketTableDisplay() {
        super(AppContextUtil.getSpringBean(ProjectFollowingTicketService.class),
                FollowingTicket.class, Arrays.asList(FollowingTicketFieldDef.summary,
                        FollowingTicketFieldDef.project, FollowingTicketFieldDef.assignee, FollowingTicketFieldDef.createdDate));

//        this.addGeneratedColumn("name", (source, itemId, columnId) -> {
//            final FollowingTicket ticket = getBeanByIndex(itemId);
//            final MButton ticketLink = new MButton(ticket.getName()).withStyleName(WebThemes.BUTTON_LINK);
//
//            if (ProjectTypeConstants.BUG.equals(ticket.getType())) {
//                ticketLink.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG));
//
//                if (StatusI18nEnum.Verified.name().equals(ticket.getStatus())) {
//                    ticketLink.addStyleName(WebThemes.LINK_COMPLETED);
//                } else if (ticket.getDueDate() != null && ticket.getDueDate().before(DateTimeUtils.getCurrentDateWithoutMS())) {
//                    ticketLink.addStyleName(WebThemes.LINK_OVERDUE);
//                }
//
//                ticketLink.addClickListener(clickEvent -> {
//                    int projectId = ticket.getProjectId();
//                    int bugId = ticket.getTypeId();
//                    PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
//                            new BugScreenData.Read(bugId));
//                    EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
//                });
//            } else if (ProjectTypeConstants.TASK.equals(ticket.getType())) {
//                ticketLink.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK));
//
//                if ("Closed".equals(ticket.getStatus())) {
//                    ticketLink.addStyleName(WebThemes.LINK_COMPLETED);
//                } else {
//                    if ("Pending".equals(ticket.getStatus())) {
//                        ticketLink.addStyleName(WebThemes.LINK_PENDING);
//                    } else if (ticket.getDueDate() != null && ticket.getDueDate().before(new GregorianCalendar().getTime())) {
//                        ticketLink.addStyleName(WebThemes.LINK_OVERDUE);
//                    }
//                }
//
//                ticketLink.addClickListener(clickEvent -> {
//                    int projectId = ticket.getProjectId();
//                    int taskId = ticket.getTypeId();
//                    PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new TaskScreenData.Read(taskId));
//                    EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
//                });
//            } else if (ProjectTypeConstants.RISK.equals(ticket.getType())) {
//                ticketLink.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.RISK));
//
//                if ("Closed".equals(ticket.getStatus())) {
//                    ticketLink.addStyleName(WebThemes.LINK_COMPLETED);
//                } else {
//                    if ("Pending".equals(ticket.getStatus())) {
//                        ticketLink.addStyleName(WebThemes.LINK_PENDING);
//                    } else if (ticket.getDueDate() != null && ticket.getDueDate().before(new GregorianCalendar().getTime())) {
//                        ticketLink.addStyleName(WebThemes.LINK_OVERDUE);
//                    }
//                }
//
//                ticketLink.addClickListener(new Button.ClickListener() {
//                    private static final long serialVersionUID = 1L;
//
//                    @Override
//                    public void buttonClick(final ClickEvent event) {
//                        final int projectId = ticket.getProjectId();
//                        final int riskId = ticket.getTypeId();
//                        final PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
//                                new RiskScreenData.Read(riskId));
//                        EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
//                    }
//                });
//            }
//
//            return ticketLink;
//        });
//
//        this.addGeneratedColumn("projectName", (source, itemId, columnId) -> {
//            final FollowingTicket ticket = getBeanByIndex(itemId);
//            Div projectLinkDiv = new Div().appendText(ProjectAssetsManager.getAsset(ProjectTypeConstants.PROJECT).getHtml() + " ")
//                    .appendChild(new A(ProjectLinkGenerator.generateProjectLink(ticket.getProjectId()))
//                            .appendText(ticket.getProjectName()));
//            return new ELabel(projectLinkDiv.write(), ContentMode.HTML).withStyleName(UIConstants.LABEL_WORD_WRAP);
//        });
//
//        this.addGeneratedColumn("assignUser", (source, itemId, columnId) -> {
//            FollowingTicket ticket = getBeanByIndex(itemId);
//            return new UserLink(ticket.getAssignUser(), ticket.getAssignUserAvatarId(), ticket.getAssignUserFullName());
//        });
//
//        this.addGeneratedColumn("monitorDate", (source, itemId, columnId) -> {
//            FollowingTicket ticket = FollowingTicketTableDisplay.this.getBeanByIndex(itemId);
//            return new ELabel().prettyDateTime(ticket.getMonitorDate());
//        });
    }
}
