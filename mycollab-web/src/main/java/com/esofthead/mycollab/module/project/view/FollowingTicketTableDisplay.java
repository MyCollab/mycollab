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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.FollowingTicket;
import com.esofthead.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.service.ProjectFollowingTicketService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.view.parameters.*;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserLink;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

import java.util.Arrays;
import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class FollowingTicketTableDisplay extends DefaultPagedBeanTable<ProjectFollowingTicketService, FollowingTicketSearchCriteria, FollowingTicket> {
    private static final long serialVersionUID = 1L;

    public FollowingTicketTableDisplay() {
        super(ApplicationContextUtil.getSpringBean(ProjectFollowingTicketService.class),
                FollowingTicket.class, Arrays.asList(FollowingTicketFieldDef.summary,
                        FollowingTicketFieldDef.project, FollowingTicketFieldDef.assignee, FollowingTicketFieldDef.createdDate));

        this.addGeneratedColumn("summary", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(final Table source, final Object itemId,
                                       final Object columnId) {
                final FollowingTicket ticket = getBeanByIndex(itemId);
                final ButtonLink ticketLink = new ButtonLink(ticket.getSummary());

                if (ProjectTypeConstants.BUG.equals(ticket.getType())) {
                    ticketLink.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG));

                    if (BugStatus.Verified.name().equals(ticket.getStatus())) {
                        ticketLink.addStyleName(UIConstants.LINK_COMPLETED);
                    } else if (ticket.getDueDate() != null && ticket.getDueDate().before(DateTimeUtils.getCurrentDateWithoutMS())) {
                        ticketLink.addStyleName(UIConstants.LINK_OVERDUE);
                    }

                    ticketLink.addClickListener(new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            final int projectId = ticket.getProjectId();
                            final int bugId = ticket.getTypeId();
                            final PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                                    new BugScreenData.Read(bugId));
                            EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
                        }
                    });
                } else if (ProjectTypeConstants.TASK.equals(ticket.getType())) {
                    ticketLink.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK));

                    if ("Closed".equals(ticket.getStatus())) {
                        ticketLink.addStyleName(UIConstants.LINK_COMPLETED);
                    } else {
                        if ("Pending".equals(ticket.getStatus())) {
                            ticketLink.addStyleName(UIConstants.LINK_PENDING);
                        } else if (ticket.getDueDate() != null && ticket.getDueDate().before(new GregorianCalendar().getTime())) {
                            ticketLink.addStyleName(UIConstants.LINK_OVERDUE);
                        }
                    }

                    ticketLink.addClickListener(new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            final int projectId = ticket.getProjectId();
                            final int taskId = ticket.getTypeId();
                            final PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new TaskScreenData.Read(taskId));
                            EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
                        }
                    });
                } else if (ProjectTypeConstants.PROBLEM.equals(ticket.getType())) {
                    ticketLink.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.PROBLEM));

                    if ("Closed".equals(ticket.getStatus())) {
                        ticketLink.addStyleName(UIConstants.LINK_COMPLETED);
                    } else {
                        if ("Pending".equals(ticket.getStatus())) {
                            ticketLink.addStyleName(UIConstants.LINK_PENDING);
                        } else if (ticket.getDueDate() != null && ticket.getDueDate().before(new GregorianCalendar().getTime())) {
                            ticketLink.addStyleName(UIConstants.LINK_OVERDUE);
                        }
                    }

                    ticketLink.addClickListener(new Button.ClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void buttonClick(final ClickEvent event) {
                            int projectId = ticket.getProjectId();
                            int problemId = ticket.getTypeId();
                            PageActionChain chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                                    new ProblemScreenData.Read(problemId));
                            EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain));
                        }
                    });
                } else if (ProjectTypeConstants.RISK.equals(ticket.getType())) {
                    ticketLink.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.RISK));

                    if ("Closed".equals(ticket.getStatus())) {
                        ticketLink.addStyleName(UIConstants.LINK_COMPLETED);
                    } else {
                        if ("Pending".equals(ticket.getStatus())) {
                            ticketLink.addStyleName(UIConstants.LINK_PENDING);
                        } else if (ticket.getDueDate() != null
                                && ticket.getDueDate().before(
                                new GregorianCalendar().getTime())) {
                            ticketLink.addStyleName(UIConstants.LINK_OVERDUE);
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
            }
        });

        this.addGeneratedColumn("projectName", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(final Table source, final Object itemId,
                                       final Object columnId) {
                final FollowingTicket ticket = getBeanByIndex(itemId);
                Div projectLinkDiv = new Div().appendText(ProjectAssetsManager.getAsset(ProjectTypeConstants.PROJECT).getHtml() + " ")
                        .appendChild(new A(ProjectLinkBuilder.generateProjectFullLink(ticket.getProjectId()))
                                .appendText(ticket.getProjectName()));
                return new ELabel(projectLinkDiv.write(), ContentMode.HTML).withStyleName(UIConstants.LABEL_WORD_WRAP);
            }
        });

        this.addGeneratedColumn("assignUser", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(final Table source, final Object itemId,
                                       final Object columnId) {
                FollowingTicket ticket = getBeanByIndex(itemId);
                return new UserLink(ticket.getAssignUser(), ticket.getAssignUserAvatarId(), ticket.getAssignUserFullName());
            }
        });

        this.addGeneratedColumn("monitorDate", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(final Table source, final Object itemId,
                                       final Object columnId) {
                FollowingTicket ticket = FollowingTicketTableDisplay.this.getBeanByIndex(itemId);
                return new ELabel().prettyDateTime(ticket.getMonitorDate());
            }
        });
    }
}
