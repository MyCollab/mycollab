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
package com.mycollab.module.project.view.milestone;

import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.Img;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.query.LazyValueInjector;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.event.MilestoneEvent;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.module.project.ui.components.BlockRowRender;
import com.mycollab.module.project.ui.components.IBlockContainer;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.project.view.service.MilestoneComponentFactory;
import com.mycollab.module.project.view.ticket.ToggleTicketSummaryField;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.ApplicationEventListener;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.AbstractLazyPageView;
import com.mycollab.vaadin.web.ui.ButtonGroup;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.addons.stackpanel.StackPanel;
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
public class MilestoneRoadmapViewImpl extends AbstractLazyPageView implements MilestoneRoadmapView, IBlockContainer {

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

    private ApplicationEventListener<MilestoneEvent.MilestoneDeleted> deletedMilestoneHandler = new
            ApplicationEventListener<MilestoneEvent.MilestoneDeleted>() {
                @Override
                @Subscribe
                public void handle(MilestoneEvent.MilestoneDeleted event) {
                    displayWidget();
                }
            };

    private MVerticalLayout roadMapView;
    private MVerticalLayout filterLayout;
    private ELabel headerText;
    private CheckBox closeMilestoneSelection, inProgressMilestoneSelection, futureMilestoneSelection;
    private MilestoneSearchCriteria baseCriteria;

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(newMilestoneHandler);
        EventBusFactory.getInstance().register(deletedMilestoneHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(newMilestoneHandler);
        EventBusFactory.getInstance().unregister(deletedMilestoneHandler);
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

        closeMilestoneSelection = new CheckBox("", true);
        inProgressMilestoneSelection = new CheckBox("", true);
        futureMilestoneSelection = new CheckBox("", true);

        closeMilestoneSelection.addValueChangeListener(valueChangeEvent ->
                displayMilestones(baseCriteria, closeMilestoneSelection.getValue(), inProgressMilestoneSelection.getValue(),
                        futureMilestoneSelection.getValue())
        );
        inProgressMilestoneSelection.addValueChangeListener(valueChangeEvent ->
                displayMilestones(baseCriteria, closeMilestoneSelection.getValue(), inProgressMilestoneSelection.getValue(),
                        futureMilestoneSelection.getValue()));
        futureMilestoneSelection.addValueChangeListener(valueChangeEvent ->
                displayMilestones(baseCriteria, closeMilestoneSelection.getValue(), inProgressMilestoneSelection.getValue(),
                        futureMilestoneSelection.getValue()));
        futureMilestoneSelection.setIcon(VaadinIcons.CLOCK);

        filterLayout.with(closeMilestoneSelection, inProgressMilestoneSelection, futureMilestoneSelection);
        displayWidget();
    }

    private void displayWidget() {
        MilestoneSearchCriteria tmpCriteria = BeanUtility.deepClone(baseCriteria);
        tmpCriteria.setStatuses(new SetSearchField<>(MilestoneStatus.Closed.name()));
        int totalCloseCount = milestoneService.getTotalCount(tmpCriteria);
        closeMilestoneSelection.setCaption(String.format("%s (%d)",
                UserUIContext.getMessage(MilestoneI18nEnum.WIDGET_CLOSED_PHASE_TITLE), totalCloseCount));
        closeMilestoneSelection.setIcon(VaadinIcons.MINUS_CIRCLE);
        filterLayout.addComponent(closeMilestoneSelection);

        tmpCriteria.setStatuses(new SetSearchField<>(MilestoneStatus.InProgress.name()));
        int totalInProgressCount = milestoneService.getTotalCount(tmpCriteria);
        inProgressMilestoneSelection.setCaption(String.format("%s (%d)",
                UserUIContext.getMessage(MilestoneI18nEnum.WIDGET_INPROGRESS_PHASE_TITLE), totalInProgressCount));
        inProgressMilestoneSelection.setIcon(VaadinIcons.SPINNER);
        filterLayout.addComponent(inProgressMilestoneSelection);

        tmpCriteria.setStatuses(new SetSearchField<>(MilestoneStatus.Future.name()));
        int totalFutureCount = milestoneService.getTotalCount(tmpCriteria);
        futureMilestoneSelection.setCaption(String.format("%s (%d)",
                UserUIContext.getMessage(MilestoneI18nEnum.WIDGET_FUTURE_PHASE_TITLE), totalFutureCount));
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
        List<SimpleMilestone> milestones = (List<SimpleMilestone>) milestoneService.findPageableListByCriteria(new BasicSearchRequest<>(baseCriteria));
        milestones.forEach(milestone -> roadMapView.addComponent(new MilestoneBlock(milestone)));

        headerText.setValue(String.format("%s %s", ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml(),
                UserUIContext.getMessage(MilestoneI18nEnum.OPT_ROADMAP_VALUE, milestones.size())));
    }

    @Override
    public void refresh() {
        headerText.setValue(String.format("%s %s", ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml(),
                UserUIContext.getMessage(MilestoneI18nEnum.OPT_ROADMAP_VALUE, roadMapView.getComponentCount())));
    }

    private void initUI() {
        headerText = ELabel.h2("");

        MHorizontalLayout headerComp = new MHorizontalLayout().withFullWidth().withMargin(true)
                .with(headerText, createHeaderRight()).withAlign(headerText, Alignment.MIDDLE_LEFT).expand(headerText);
        this.addComponent(headerComp);
        roadMapView = new MVerticalLayout().withSpacing(false).withMargin(false);
        filterLayout = new MVerticalLayout();

        MHorizontalLayout bodyComp = new MHorizontalLayout(roadMapView).withFullWidth().withMargin(true).expand(roadMapView);
        this.with(headerComp, bodyComp).expand(bodyComp);

        ProjectView projectView = UIUtils.getRoot(this, ProjectView.class);
        Panel filterPanel = new Panel("Filter by status", filterLayout);
        StackPanel.extend(filterPanel);
        projectView.addComponentToRightBar(filterPanel);
    }

    private HorizontalLayout createHeaderRight() {
        MButton createBtn = new MButton(UserUIContext.getMessage(MilestoneI18nEnum.NEW), clickEvent -> {
            SimpleMilestone milestone = new SimpleMilestone();
            milestone.setSaccountid(AppUI.getAccountId());
            milestone.setProjectid(CurrentProjectVariables.getProjectId());
            UI.getCurrent().addWindow(new MilestoneAddWindow(milestone));
        }).withIcon(VaadinIcons.PLUS).withStyleName(WebThemes.BUTTON_ACTION)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES));

        MButton printBtn = new MButton("", clickEvent ->
                UI.getCurrent().addWindow(new MilestoneCustomizeReportOutputWindow(new LazyValueInjector() {
                    @Override
                    protected Object doEval() {
                        return baseCriteria;
                    }
                }))
        ).withIcon(VaadinIcons.PRINT).withStyleName(WebThemes.BUTTON_OPTION)
                .withDescription(UserUIContext.getMessage(GenericI18Enum.ACTION_EXPORT));

        MButton boardBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_BOARD), clickEvent ->
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null))).withIcon(VaadinIcons.SERVER).withWidth("100px");

        MButton roadmapBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_LIST)).withIcon
                (VaadinIcons.BULLETS).withWidth("100px");

        return new MHorizontalLayout(createBtn, printBtn,
                new ButtonGroup(roadmapBtn, boardBtn).withDefaultButton(roadmapBtn));
    }

    private static class MilestoneBlock extends BlockRowRender {
        private boolean showIssues = false;

        MilestoneBlock(final SimpleMilestone milestone) {
            this.withMargin(new MarginInfo(true, false, true, false)).withStyleName("roadmap-block");

            VaadinIcons statusIcon = ProjectAssetsUtil.getPhaseIcon(milestone.getStatus());
            ELabel statusLbl = ELabel.html(statusIcon.getHtml() + " " + UserUIContext.getMessage(MilestoneStatus.class,
                    milestone.getStatus())).withStyleName(WebThemes.BLOCK).withUndefinedWidth();
            ToggleMilestoneSummaryField toggleMilestoneSummaryField = new ToggleMilestoneSummaryField(milestone, false, true);
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

            this.with(metaBlock);

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
                                * 100 / totalAssignments)).withStyleName(WebThemes.META_INFO);
            } else {
                progressInfoLbl = new ELabel(UserUIContext.getMessage(ProjectI18nEnum.OPT_NO_TICKET))
                        .withStyleName(WebThemes.META_INFO);
            }

            final MVerticalLayout issueLayout = new MVerticalLayout().withMargin(new MarginInfo(false, true, false, true));
            issueLayout.setVisible(false);

            progressLayout.with(progressInfoLbl);

            if (totalAssignments > 0) {
                final MButton viewIssuesBtn = new MButton(UserUIContext.getMessage(ProjectI18nEnum.ACTION_VIEW_TICKETS))
                        .withStyleName(WebThemes.BUTTON_LINK);
                viewIssuesBtn.addClickListener(clickEvent -> {
                    showIssues = !showIssues;
                    if (showIssues) {
                        issueLayout.setVisible(true);
                        viewIssuesBtn.setCaption(UserUIContext.getMessage(ProjectI18nEnum.ACTION_HIDE_TICKETS));
                        ProjectTicketSearchCriteria searchCriteria = new ProjectTicketSearchCriteria();
                        searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                        searchCriteria.setTypes(CurrentProjectVariables.getRestrictedTicketTypes());
                        searchCriteria.setMilestoneId(new NumberSearchField(milestone.getId()));
                        ProjectTicketService genericTaskService = AppContextUtil.getSpringBean(ProjectTicketService.class);
                        List<ProjectTicket> tickets = (List<ProjectTicket>) genericTaskService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
                        for (ProjectTicket ticket : tickets) {
                            ToggleTicketSummaryField toggleTicketSummaryField = new ToggleTicketSummaryField(ticket);
                            MHorizontalLayout rowComp = new MHorizontalLayout(ELabel.EMPTY_SPACE());
                            rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);
                            rowComp.with(ELabel.fontIcon(ProjectAssetsManager.getAsset(ticket.getType())).withUndefinedWidth());
                            String status = "";
                            if (ticket.isMilestone()) {
                                status = UserUIContext.getMessage(MilestoneStatus.class, ticket.getStatus());
                            } else {
                                status = UserUIContext.getMessage(StatusI18nEnum.class, ticket.getStatus());
                            }
                            rowComp.with(new ELabel(status).withStyleName(WebThemes.BLOCK).withUndefinedWidth());
                            String avatarLink = StorageUtils.getAvatarPath(ticket.getAssignUserAvatarId(), 16);
                            Img img = new Img(ticket.getAssignUserFullName(), avatarLink).setCSSClass(WebThemes.CIRCLE_BOX)
                                    .setTitle(ticket.getAssignUserFullName());
                            rowComp.with(ELabel.html(img.write()).withUndefinedWidth());

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
