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

import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.ProjectViewHeader;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractLazyPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.ToggleButtonGroup;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
@ViewComponent
public class MilestoneRoadmapViewImpl extends AbstractLazyPageView implements MilestoneRoadmapView {
    private Button createBtn;

    private MilestoneService milestoneService = ApplicationContextUtil.getSpringBean(MilestoneService.class);
    private MilestoneSearchCriteria baseCriteria;

    private ApplicationEventListener<MilestoneEvent.NewMilestoneAdded> newMilestoneHandler = new
            ApplicationEventListener<MilestoneEvent.NewMilestoneAdded>() {
                @Override
                @Subscribe
                public void handle(MilestoneEvent.NewMilestoneAdded event) {
                    MilestoneRoadmapViewImpl.this.removeAllComponents();
                    displayView();
                }
            };

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(newMilestoneHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(newMilestoneHandler);
        super.detach();
    }

    @Override
    protected void displayView() {
        initUI();
        createBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES));

        new Thread() {
            public void run() {
                UI.getCurrent().access(new Runnable() {
                    @Override
                    public void run() {
                        baseCriteria = new MilestoneSearchCriteria();
                        baseCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
                        baseCriteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("startdate",
                                SearchCriteria.DESC), new SearchCriteria.OrderField("enddate", SearchCriteria.DESC)));
                        List<SimpleMilestone> milestones = milestoneService.findPagableListByCriteria(new SearchRequest<>(baseCriteria, 0, Integer.MAX_VALUE));
                        for (SimpleMilestone milestone : milestones) {
                            MilestoneRoadmapViewImpl.this.addComponent(new MilestoneBlock(milestone));
                        }
                        UI.getCurrent().push();
                    }
                });
            }
        }.start();
    }

    private void initUI() {
        ProjectViewHeader headerText = new ProjectViewHeader(ProjectTypeConstants.MILESTONE, "Roadmap");

        MHorizontalLayout header = new MHorizontalLayout()
                .withStyleName("hdr-view").withWidth("100%").withMargin(true)
                .with(headerText, createHeaderRight())
                .withAlign(headerText, Alignment.MIDDLE_LEFT).expand(headerText);
        this.addComponent(header);
    }

    private HorizontalLayout createHeaderRight() {
        MHorizontalLayout layout = new MHorizontalLayout();

        createBtn = new Button(AppContext.getMessage(MilestoneI18nEnum.BUTTON_NEW_PHASE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final Button.ClickEvent event) {
                UI.getCurrent().addWindow(new MilestoneAddWindow(new SimpleMilestone()));
            }
        });
        createBtn.setIcon(FontAwesome.PLUS);
        createBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        createBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES));
        layout.with(createBtn);

        Button kanbanBtn = new Button(null, new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(MilestoneRoadmapViewImpl.this, null));
            }
        });
        kanbanBtn.setDescription("Kanban View");
        kanbanBtn.setIcon(FontAwesome.TH);

        Button roadmapBtn = new Button();
        roadmapBtn.setDescription("Roadmap");
        roadmapBtn.setIcon(VaadinIcons.CUBE);

        ToggleButtonGroup viewButtons = new ToggleButtonGroup();
        viewButtons.addButton(roadmapBtn);
        viewButtons.addButton(kanbanBtn);
        viewButtons.setDefaultButton(roadmapBtn);
        layout.with(viewButtons);

        return layout;
    }

    private static class MilestoneBlock extends MVerticalLayout {
        private boolean showIssues = false;
        private MVerticalLayout issueLayout;

        MilestoneBlock(final SimpleMilestone milestone) {
            this.setStyleName("roadmap-block");
            Div milestoneDiv = new Div().appendText(VaadinIcons.CALENDAR_BRIEFCASE.getHtml() + " ").appendChild(new A
                    (ProjectLinkBuilder.generateMilestonePreviewFullLink(milestone.getProjectid(), milestone.getId()))
                    .appendText(milestone.getName())).appendText(" (" + AppContext.getMessage(com.esofthead.mycollab
                    .module.project.i18n.OptionI18nEnum.MilestoneStatus.class, milestone
                    .getStatus()) + ")");
            ELabel milestoneLbl = new ELabel(milestoneDiv.write(), ContentMode.HTML).withStyleName("h2");
            this.addComponent(milestoneLbl);

            MHorizontalLayout metaBlock = new MHorizontalLayout();
            Div userDiv = new Div().appendChild(new Img("", StorageFactory.getInstance().getAvatarPath(milestone
                    .getOwnerAvatarId(), 16))).appendChild(new A(ProjectLinkBuilder.generateProjectMemberFullLink
                    (milestone.getProjectid(), milestone.getOwner())).appendText(" " + StringUtils.trim
                    (milestone.getOwnerFullName(), 20, true)));
            metaBlock.addComponent(new ELabel(userDiv.write(), ContentMode.HTML).withStyleName("block"));
            metaBlock.addComponent(new ELabel("Start: " + AppContext.formatDate(milestone.getStartdate()))
                    .withStyleName("block").withDescription("Start date"));
            metaBlock.addComponent(new ELabel("End: " + AppContext.formatDate(milestone.getEnddate())).withStyleName
                    ("block").withDescription("End date"));
            metaBlock.addComponent(new ELabel(FontAwesome.MONEY.getHtml() + " " + (milestone.getTotalBugBillableHours() + milestone
                    .getTotalTaskBillableHours()), ContentMode.HTML).withStyleName("block").withDescription
                    ("Billable hours"));
            metaBlock.addComponent(new ELabel(FontAwesome.GIFT.getHtml() + " " + (milestone.getTotalBugNonBillableHours() + milestone
                    .getTotalTaskNonBillableHours()), ContentMode.HTML).withStyleName("block").withDescription("Non " +
                    "billable hours"));
            this.add(metaBlock);

            ELabel descriptionLbl = new ELabel(StringUtils.formatRichText(milestone.getDescription()), ContentMode
                    .HTML).withStyleName("meta");
            this.addComponent(descriptionLbl);

            MHorizontalLayout progressLayout = new MHorizontalLayout();
            int openAssignments = milestone.getNumOpenBugs() + milestone.getNumOpenTasks();
            int totalAssignments = milestone.getNumBugs() + milestone.getNumTasks();
            ELabel progressInfoLbl;
            if (totalAssignments > 0) {
                progressInfoLbl = new ELabel(String.format("%d of %d issue(s) resolved. Progress (%d%%)",
                        (totalAssignments - openAssignments), totalAssignments, (totalAssignments - openAssignments)
                                * 100 / totalAssignments)).withStyleName("meta");
            } else {
                progressInfoLbl = new ELabel("No issue").withStyleName("meta");
            }

            final Button viewIssuesBtn = new Button("View issues");
            Button.ClickListener viewIssuesListener = new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    showIssues = !showIssues;
                    if (showIssues) {
                        issueLayout.setVisible(true);
                        viewIssuesBtn.setCaption("Hide issues");
                        ProjectGenericTaskSearchCriteria searchCriteria = new ProjectGenericTaskSearchCriteria();
                        searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                        searchCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK));
                        searchCriteria.setMilestoneId(new NumberSearchField(milestone.getId()));
                        ProjectGenericTaskService genericTaskService = ApplicationContextUtil.getSpringBean
                                (ProjectGenericTaskService.class);
                        List<ProjectGenericTask> genericTasks = genericTaskService.findPagableListByCriteria(new
                                SearchRequest<>(searchCriteria, 0, Integer.MAX_VALUE));
                        for (ProjectGenericTask genericTask : genericTasks) {
                            Div issueDiv = new Div();
                            issueDiv.appendText(ProjectAssetsManager.getAsset(genericTask.getType()).getHtml() + " ");
                            String uid = UUID.randomUUID().toString();
                            A taskLink = new A().setId("tag" + uid);
                            taskLink.setHref(ProjectLinkBuilder.generateProjectItemLink(genericTask.getProjectShortName(),
                                    genericTask.getProjectId(), genericTask.getType(), genericTask.getExtraTypeId() + ""));
                            taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, genericTask.getType(), genericTask.getTypeId() + ""));
                            taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
                            taskLink.appendText(String.format("[#%d] - %s", genericTask.getExtraTypeId(), genericTask.getName()));
                            issueDiv.appendChild(taskLink, TooltipHelper.buildDivTooltipEnable(uid));
                            Label issueLbl = new Label(issueDiv.write(), ContentMode.HTML);
                            issueLayout.addComponent(issueLbl);
                            if (genericTask.isClosed()) {
                                issueLbl.addStyleName("completed");
                            } else if (genericTask.isOverdue()) {
                                issueLbl.addStyleName("overdue");
                            }
                        }
                    } else {
                        viewIssuesBtn.setCaption("View issues");
                        issueLayout.removeAllComponents();
                        issueLayout.setVisible(false);
                    }
                }
            };
            viewIssuesBtn.addClickListener(viewIssuesListener);
            viewIssuesBtn.setStyleName(UIConstants.THEME_LINK);
            viewIssuesBtn.addStyleName("block");
            progressLayout.with(progressInfoLbl, viewIssuesBtn);
            this.addComponent(progressLayout);
            issueLayout = new MVerticalLayout().withMargin(new MarginInfo(false, true, false, true));
            issueLayout.setVisible(false);
            this.addComponent(issueLayout);
        }
    }
}
