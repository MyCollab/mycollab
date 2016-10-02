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
package com.mycollab.module.project.view.milestone;

import com.esofthead.vaadin.floatingcomponent.FloatingComponent;
import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.Img;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.query.LazyValueInjector;
import com.mycollab.eventmanager.ApplicationEventListener;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.MilestoneEvent;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.service.MilestoneComponentFactory;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.view.AbstractLazyPageView;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.ToggleButtonGroup;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
@ViewComponent
public class MilestoneRoadmapViewImpl extends AbstractLazyPageView implements MilestoneRoadmapView {
    private MButton createBtn;

    private MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);

    private ApplicationEventListener<MilestoneEvent.NewMilestoneAdded> newMilestoneHandler = new
            ApplicationEventListener<MilestoneEvent.NewMilestoneAdded>() {
                @Override
                @Subscribe
                public void handle(MilestoneEvent.NewMilestoneAdded event) {
                    MilestoneRoadmapViewImpl.this.removeAllComponents();
                    displayView();
                }
            };

    private MVerticalLayout roadMapView;
    private VerticalLayout filterPanel;
    private ELabel headerText;
    private MilestoneSearchCriteria baseCriteria;

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

        baseCriteria = new MilestoneSearchCriteria();
        baseCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        baseCriteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("startdate",
                SearchCriteria.DESC), new SearchCriteria.OrderField("enddate", SearchCriteria.DESC)));
        displayMilestones();

        final MilestoneSearchCriteria tmpCriteria = BeanUtility.deepClone(baseCriteria);
        tmpCriteria.setStatuses(new SetSearchField<>(MilestoneStatus.Closed.name()));
        int totalCloseCount = milestoneService.getTotalCount(tmpCriteria);
        final CheckBox closeMilestoneSelection = new CheckBox(String.format("%s (%d)",
                UserUIContext.getMessage(MilestoneI18nEnum.WIDGET_CLOSED_PHASE_TITLE), totalCloseCount), true);
        closeMilestoneSelection.setIcon(FontAwesome.MINUS_CIRCLE);
        filterPanel.addComponent(closeMilestoneSelection);

        tmpCriteria.setStatuses(new SetSearchField<>(MilestoneStatus.InProgress.name()));
        int totalInProgressCount = milestoneService.getTotalCount(tmpCriteria);
        final CheckBox inProgressMilestoneSelection = new CheckBox(String.format("%s (%d)",
                UserUIContext.getMessage(MilestoneI18nEnum.WIDGET_INPROGRESS_PHASE_TITLE), totalInProgressCount), true);
        inProgressMilestoneSelection.setIcon(FontAwesome.SPINNER);
        filterPanel.addComponent(inProgressMilestoneSelection);

        tmpCriteria.setStatuses(new SetSearchField<>(MilestoneStatus.Future.name()));
        int totalFutureCount = milestoneService.getTotalCount(tmpCriteria);
        final CheckBox futureMilestoneSelection = new CheckBox(String.format("%s (%d)",
                UserUIContext.getMessage(MilestoneI18nEnum.WIDGET_FUTURE_PHASE_TITLE), totalFutureCount), true);

        closeMilestoneSelection.addValueChangeListener(valueChangeEvent ->
                displayMilestones(tmpCriteria, closeMilestoneSelection.getValue(), inProgressMilestoneSelection.getValue(),
                        futureMilestoneSelection.getValue())
        );
        inProgressMilestoneSelection.addValueChangeListener(valueChangeEvent ->
                displayMilestones(tmpCriteria, closeMilestoneSelection.getValue(), inProgressMilestoneSelection.getValue(),
                        futureMilestoneSelection.getValue()));
        futureMilestoneSelection.addValueChangeListener(valueChangeEvent ->
                displayMilestones(tmpCriteria, closeMilestoneSelection.getValue(), inProgressMilestoneSelection.getValue(),
                        futureMilestoneSelection.getValue()));
        futureMilestoneSelection.setIcon(FontAwesome.CLOCK_O);
        filterPanel.addComponent(futureMilestoneSelection);
    }

    private void displayMilestones(MilestoneSearchCriteria milestoneSearchCriteria, boolean closeSelection, boolean
            inProgressSelection, boolean futureSelection) {
        baseCriteria = milestoneSearchCriteria;
        List<String> statuses = new ArrayList<>();
        if (closeSelection) {
            statuses.add(MilestoneStatus.Closed.name());
        }
        if (inProgressSelection) {
            statuses.add(MilestoneStatus.InProgress.name());
        }
        if (futureSelection) {
            statuses.add(MilestoneStatus.Future.name());
        }
        if (statuses.size() > 0) {
            baseCriteria.setStatuses(new SetSearchField<>(statuses));
            displayMilestones();
        } else {
            roadMapView.removeAllComponents();
        }
    }

    private void displayMilestones() {
        roadMapView.removeAllComponents();
        List<SimpleMilestone> milestones = milestoneService.findPageableListByCriteria(new BasicSearchRequest<>(baseCriteria));
        for (SimpleMilestone milestone : milestones) {
            roadMapView.addComponent(new MilestoneBlock(milestone));
        }

        headerText.setValue(String.format("%s %s", ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml(),
                UserUIContext.getMessage(MilestoneI18nEnum.OPT_ROADMAP_VALUE, milestones.size())));
    }

    private void initUI() {
        headerText = ELabel.h2("");

        MHorizontalLayout header = new MHorizontalLayout().withStyleName("hdr-view").withFullWidth().withMargin(true)
                .with(headerText, createHeaderRight()).withAlign(headerText, Alignment.MIDDLE_LEFT).expand(headerText);
        this.addComponent(header);
        roadMapView = new MVerticalLayout().withSpacing(false);
        filterPanel = new MVerticalLayout().withWidth("250px").withStyleName(WebUIConstants.BOX);
        FloatingComponent floatingComponent = FloatingComponent.floatThis(filterPanel);
        floatingComponent.setContainerId("main-body");
        this.addComponent(new MHorizontalLayout().withFullWidth().with(roadMapView, filterPanel).expand(roadMapView));
    }

    private HorizontalLayout createHeaderRight() {
        createBtn = new MButton(UserUIContext.getMessage(MilestoneI18nEnum.NEW), clickEvent -> {
            SimpleMilestone milestone = new SimpleMilestone();
            milestone.setSaccountid(MyCollabUI.getAccountId());
            milestone.setProjectid(CurrentProjectVariables.getProjectId());
            UI.getCurrent().addWindow(new MilestoneAddWindow(milestone));
        }).withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES));

        MButton printBtn = new MButton("", clickEvent ->
                UI.getCurrent().addWindow(new MilestoneCustomizeReportOutputWindow(new LazyValueInjector() {
                    @Override
                    protected Object doEval() {
                        return baseCriteria;
                    }
                }))
        ).withIcon(FontAwesome.PRINT).withStyleName(WebUIConstants.BUTTON_OPTION)
                .withDescription(UserUIContext.getMessage(GenericI18Enum.ACTION_EXPORT));

        MButton boardBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_BOARD), clickEvent ->
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null))).withIcon(FontAwesome.SERVER).withWidth("100px");

        MButton roadmapBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_LIST)).withIcon
                (FontAwesome.NAVICON).withWidth("100px");

        ToggleButtonGroup viewButtons = new ToggleButtonGroup();
        viewButtons.addButton(roadmapBtn);
        viewButtons.addButton(boardBtn);
        if (!SiteConfiguration.isCommunityEdition()) {
            MButton kanbanBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN),
                    clickEvent -> EventBusFactory.getInstance().post(new MilestoneEvent.GotoKanban(MilestoneRoadmapViewImpl.this)))
                    .withIcon(FontAwesome.TH).withWidth("100px");
            viewButtons.addButton(kanbanBtn);
        }

        viewButtons.withDefaultButton(roadmapBtn);
        return new MHorizontalLayout(createBtn, printBtn, viewButtons);
    }

    private static class MilestoneBlock extends MVerticalLayout {
        private boolean showIssues = false;

        MilestoneBlock(final SimpleMilestone milestone) {
            this.withMargin(new MarginInfo(true, false, true, false)).withStyleName("roadmap-block");

            ELabel statusLbl = new ELabel(UserUIContext.getMessage(MilestoneStatus.class, milestone.getStatus()))
                    .withStyleName(WebUIConstants.BLOCK).withWidthUndefined();
            ToggleMilestoneSummaryField toggleMilestoneSummaryField = new ToggleMilestoneSummaryField(milestone);
            MHorizontalLayout headerLayout = new MHorizontalLayout(statusLbl, toggleMilestoneSummaryField).expand
                    (toggleMilestoneSummaryField).withFullWidth();
            this.with(headerLayout);

            CssLayout metaBlock = new CssLayout();
            MilestoneComponentFactory popupFieldFactory = AppContextUtil.getSpringBean(MilestoneComponentFactory.class);
            metaBlock.addComponent(popupFieldFactory.createMilestoneAssigneePopupField(milestone, true));
            metaBlock.addComponent(popupFieldFactory.createStartDatePopupField(milestone));
            metaBlock.addComponent(popupFieldFactory.createEndDatePopupField(milestone));
            if (!SiteConfiguration.isCommunityEdition()) {
                metaBlock.addComponent(popupFieldFactory.createBillableHoursPopupField(milestone));
                metaBlock.addComponent(popupFieldFactory.createNonBillableHoursPopupField(milestone));
            }

            this.add(metaBlock);

            if (StringUtils.isNotBlank(milestone.getDescription())) {
                this.addComponent(ELabel.html(StringUtils.formatRichText(milestone.getDescription())));
            }

            MHorizontalLayout progressLayout = new MHorizontalLayout();
            progressLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            int openAssignments = milestone.getNumOpenBugs() + milestone.getNumOpenTasks() + milestone.getNumOpenRisks();
            int totalAssignments = milestone.getNumBugs() + milestone.getNumTasks() + milestone.getNumRisks();
            ELabel progressInfoLbl;
            if (totalAssignments > 0) {
                progressInfoLbl = new ELabel(UserUIContext.getMessage(ProjectI18nEnum.OPT_PROJECT_TICKET,
                        (totalAssignments - openAssignments), totalAssignments, (totalAssignments - openAssignments)
                                * 100 / totalAssignments)).withStyleName(UIConstants.META_INFO);
            } else {
                progressInfoLbl = new ELabel(UserUIContext.getMessage(ProjectI18nEnum.OPT_NO_TICKET))
                        .withStyleName(UIConstants.META_INFO);
            }

            final MVerticalLayout issueLayout = new MVerticalLayout().withMargin(new MarginInfo(false, true, false, true));
            issueLayout.setVisible(false);

            progressLayout.with(progressInfoLbl);

            if (totalAssignments > 0) {
                final MButton viewIssuesBtn = new MButton(UserUIContext.getMessage(ProjectI18nEnum.ACTION_VIEW_TICKETS))
                        .withStyleName(WebUIConstants.BUTTON_LINK);
                viewIssuesBtn.addClickListener(clickEvent -> {
                    showIssues = !showIssues;
                    if (showIssues) {
                        issueLayout.setVisible(true);
                        viewIssuesBtn.setCaption(UserUIContext.getMessage(ProjectI18nEnum.ACTION_HIDE_TICKETS));
                        ProjectTicketSearchCriteria searchCriteria = new ProjectTicketSearchCriteria();
                        searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                        searchCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK,
                                ProjectTypeConstants.RISK));
                        searchCriteria.setMilestoneId(new NumberSearchField(milestone.getId()));
                        ProjectTicketService genericTaskService = AppContextUtil.getSpringBean(ProjectTicketService.class);
                        List<ProjectTicket> tickets = genericTaskService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
                        for (ProjectTicket ticket : tickets) {
                            ToggleTicketSummaryField toggleTicketSummaryField = new ToggleTicketSummaryField(ticket);
                            MHorizontalLayout rowComp = new MHorizontalLayout();
                            rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);
                            rowComp.with(ELabel.fontIcon(ProjectAssetsManager.getAsset(ticket.getType())).withWidthUndefined());
                            String status = "";
                            if (ticket.isBug()) {
                                status = UserUIContext.getMessage(BugStatus.class, ticket.getStatus());
                            } else if (ticket.isMilestone()) {
                                status = UserUIContext.getMessage(MilestoneStatus.class, ticket.getStatus());
                            } else if (ticket.isRisk()) {
                                status = UserUIContext.getMessage(StatusI18nEnum.class, ticket.getStatus());
                            } else if (ticket.isTask()) {
                                status = UserUIContext.getMessage(StatusI18nEnum.class, ticket.getStatus());
                            }
                            rowComp.with(new ELabel(status).withStyleName(WebUIConstants.BLOCK).withWidthUndefined());
                            String avatarLink = StorageFactory.getAvatarPath(ticket.getAssignUserAvatarId(), 16);
                            Img img = new Img(ticket.getAssignUserFullName(), avatarLink).setCSSClass(UIConstants.CIRCLE_BOX)
                                    .setTitle(ticket.getAssignUserFullName());
                            rowComp.with(ELabel.html(img.write()).withWidthUndefined());

                            rowComp.with(toggleTicketSummaryField).expand(toggleTicketSummaryField);
                            issueLayout.addComponent(rowComp);
                        }
                    } else {
                        viewIssuesBtn.setCaption(UserUIContext.getMessage(ProjectI18nEnum.ACTION_VIEW_TICKETS));
                        issueLayout.removeAllComponents();
                        issueLayout.setVisible(false);
                    }
                });
                progressLayout.with(viewIssuesBtn);
            }

            this.with(progressLayout, issueLayout);
        }
    }
}
