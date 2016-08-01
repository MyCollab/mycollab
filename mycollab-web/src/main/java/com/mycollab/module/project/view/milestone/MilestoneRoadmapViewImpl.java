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
import com.mycollab.module.project.domain.ProjectGenericTask;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.mycollab.module.project.events.MilestoneEvent;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.module.project.service.ProjectGenericTaskService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.view.AbstractLazyPageView;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.ToggleButtonGroup;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.teemu.VaadinIcons;
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
        createBtn.setVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES));

        baseCriteria = new MilestoneSearchCriteria();
        baseCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        baseCriteria.setOrderFields(Arrays.asList(new SearchCriteria.OrderField("startdate",
                SearchCriteria.DESC), new SearchCriteria.OrderField("enddate", SearchCriteria.DESC)));
        displayMilestones();

        final MilestoneSearchCriteria tmpCriteria = BeanUtility.deepClone(baseCriteria);
        tmpCriteria.setStatuses(new SetSearchField<>(OptionI18nEnum.MilestoneStatus.Closed.name()));
        int totalCloseCount = milestoneService.getTotalCount(tmpCriteria);
        final CheckBox closeMilestoneSelection = new CheckBox(AppContext.getMessage(MilestoneI18nEnum
                .WIDGET_CLOSED_PHASE_TITLE) + " (" + totalCloseCount + ")", true);
        closeMilestoneSelection.setIcon(FontAwesome.MINUS_CIRCLE);
        filterPanel.addComponent(closeMilestoneSelection);

        tmpCriteria.setStatuses(new SetSearchField<>(OptionI18nEnum.MilestoneStatus.InProgress.name()));
        int totalInProgressCount = milestoneService.getTotalCount(tmpCriteria);
        final CheckBox inProgressMilestoneSelection = new CheckBox(AppContext.getMessage(MilestoneI18nEnum
                .WIDGET_INPROGRESS_PHASE_TITLE) + " (" + totalInProgressCount + ")", true);
        inProgressMilestoneSelection.setIcon(FontAwesome.SPINNER);
        filterPanel.addComponent(inProgressMilestoneSelection);

        tmpCriteria.setStatuses(new SetSearchField<>(OptionI18nEnum.MilestoneStatus.Future.name()));
        int totalFutureCount = milestoneService.getTotalCount(tmpCriteria);
        final CheckBox futureMilestoneSelection = new CheckBox(AppContext.getMessage(MilestoneI18nEnum
                .WIDGET_FUTURE_PHASE_TITLE) + " (" + totalFutureCount + ")", true);

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
            statuses.add(OptionI18nEnum.MilestoneStatus.Closed.name());
        }
        if (inProgressSelection) {
            statuses.add(OptionI18nEnum.MilestoneStatus.InProgress.name());
        }
        if (futureSelection) {
            statuses.add(OptionI18nEnum.MilestoneStatus.Future.name());
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
        List<SimpleMilestone> milestones = milestoneService.findPageableListByCriteria(new BasicSearchRequest<>(baseCriteria, 0,
                Integer.MAX_VALUE));
        for (SimpleMilestone milestone : milestones) {
            roadMapView.addComponent(new MilestoneBlock(milestone));
        }

        headerText.setValue(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml() + " Roadmap ("
                + milestones.size() + ")");
    }

    private void initUI() {
        headerText = ELabel.h2("");

        MHorizontalLayout header = new MHorizontalLayout().withStyleName("hdr-view").withFullWidth().withMargin(true)
                .with(headerText, createHeaderRight())
                .withAlign(headerText, Alignment.MIDDLE_LEFT).expand(headerText);
        this.addComponent(header);
        roadMapView = new MVerticalLayout().withSpacing(false);
        filterPanel = new MVerticalLayout().withWidth("250px").withStyleName(WebUIConstants.BOX);
        FloatingComponent floatingComponent = FloatingComponent.floatThis(filterPanel);
        floatingComponent.setContainerId("main-body");
        this.addComponent(new MHorizontalLayout().withFullWidth().with(roadMapView, filterPanel).expand(roadMapView));
    }

    private HorizontalLayout createHeaderRight() {
        MHorizontalLayout layout = new MHorizontalLayout();

        createBtn = new MButton(AppContext.getMessage(MilestoneI18nEnum.NEW), clickEvent -> {
            SimpleMilestone milestone = new SimpleMilestone();
            milestone.setSaccountid(AppContext.getAccountId());
            milestone.setProjectid(CurrentProjectVariables.getProjectId());
            UI.getCurrent().addWindow(new MilestoneAddWindow(milestone));
        }).withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION);
        createBtn.setVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES));
        layout.with(createBtn);

        MButton printBtn = new MButton("", clickEvent -> {
            UI.getCurrent().addWindow(new MilestoneCustomizeReportOutputWindow(new LazyValueInjector() {
                @Override
                protected Object doEval() {
                    return baseCriteria;
                }
            }));
        }).withIcon(FontAwesome.PRINT).withStyleName(WebUIConstants.BUTTON_OPTION)
                .withDescription(AppContext.getMessage(GenericI18Enum.ACTION_EXPORT));

        layout.addComponent(printBtn);

        Button kanbanBtn = new Button("Board", clickEvent -> EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(this, null)));
        kanbanBtn.setDescription("Board View");
        kanbanBtn.setIcon(FontAwesome.TH);

        Button roadmapBtn = new Button("List");
        roadmapBtn.setDescription("Roadmap");
        roadmapBtn.setIcon(VaadinIcons.CUBE);

        ToggleButtonGroup viewButtons = new ToggleButtonGroup();
        viewButtons.addButton(roadmapBtn);
        viewButtons.addButton(kanbanBtn);
        viewButtons.withDefaultButton(roadmapBtn);
        layout.with(viewButtons);

        return layout;
    }

    private static class MilestoneBlock extends MVerticalLayout {
        private boolean showIssues = false;

        MilestoneBlock(final SimpleMilestone milestone) {
            this.setMargin(new MarginInfo(true, false, true, false));
            this.setStyleName("roadmap-block");

            ELabel statusLbl = new ELabel(AppContext.getMessage(OptionI18nEnum.MilestoneStatus.class, milestone.getStatus()))
                    .withStyleName(WebUIConstants.FIELD_NOTE).withWidthUndefined();
            ToggleMilestoneSummaryField toggleMilestoneSummaryField = new ToggleMilestoneSummaryField(milestone);
            MHorizontalLayout headerLayout = new MHorizontalLayout(statusLbl, toggleMilestoneSummaryField).expand
                    (toggleMilestoneSummaryField).withFullWidth();
            this.with(headerLayout);

            CssLayout metaBlock = new CssLayout();
            MilestonePopupFieldFactory popupFieldFactory = ViewManager.getCacheComponent(MilestonePopupFieldFactory.class);
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
            int openAssignments = milestone.getNumOpenBugs() + milestone.getNumOpenTasks();
            int totalAssignments = milestone.getNumBugs() + milestone.getNumTasks();
            ELabel progressInfoLbl;
            if (totalAssignments > 0) {
                progressInfoLbl = new ELabel(AppContext.getMessage(ProjectI18nEnum.OPT_PROJECT_ASSIGNMENT,
                        (totalAssignments - openAssignments), totalAssignments, (totalAssignments - openAssignments)
                                * 100 / totalAssignments)).withStyleName(UIConstants.META_INFO);
            } else {
                progressInfoLbl = new ELabel(AppContext.getMessage(ProjectI18nEnum.OPT_NO_ASSIGNMENT))
                        .withStyleName(UIConstants.META_INFO);
            }

            final MVerticalLayout issueLayout = new MVerticalLayout().withMargin(new MarginInfo(false, true, false, true));
            issueLayout.setVisible(false);

            progressLayout.with(progressInfoLbl);

            if (totalAssignments > 0) {
                final Button viewIssuesBtn = new Button(AppContext.getMessage(ProjectI18nEnum.ACTION_VIEW_ASSIGNMENTS));
                viewIssuesBtn.addClickListener(clickEvent -> {
                    showIssues = !showIssues;
                    if (showIssues) {
                        issueLayout.setVisible(true);
                        viewIssuesBtn.setCaption(AppContext.getMessage(ProjectI18nEnum.ACTION_HIDE_ASSIGNMENTS));
                        ProjectGenericTaskSearchCriteria searchCriteria = new ProjectGenericTaskSearchCriteria();
                        searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                        searchCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK));
                        searchCriteria.setMilestoneId(new NumberSearchField(milestone.getId()));
                        ProjectGenericTaskService genericTaskService = AppContextUtil.getSpringBean
                                (ProjectGenericTaskService.class);
                        List<ProjectGenericTask> genericTasks = genericTaskService.findPageableListByCriteria(new
                                BasicSearchRequest<>(searchCriteria, 0, Integer.MAX_VALUE));
                        for (ProjectGenericTask genericTask : genericTasks) {
                            ToggleGenericTaskSummaryField toggleGenericTaskSummaryField = new ToggleGenericTaskSummaryField(genericTask);
                            MHorizontalLayout rowComp = new MHorizontalLayout();
                            rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);
                            rowComp.with(ELabel.fontIcon(ProjectAssetsManager.getAsset(genericTask.getType())).withWidthUndefined());
                            String status = "";
                            if (genericTask.isBug()) {
                                status = AppContext.getMessage(OptionI18nEnum.BugStatus.class, genericTask.getStatus());
                            } else if (genericTask.isMilestone()) {
                                status = AppContext.getMessage(OptionI18nEnum.MilestoneStatus.class, genericTask.getStatus());
                            } else if (genericTask.isRisk()) {
                                status = AppContext.getMessage(com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.class,
                                        genericTask.getStatus());
                            } else if (genericTask.isTask()) {
                                status = AppContext.getMessage(com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.class, genericTask.getStatus());
                            }
                            rowComp.with(new ELabel(status).withStyleName(WebUIConstants.FIELD_NOTE).withWidthUndefined());
                            String avatarLink = StorageFactory.getAvatarPath(genericTask.getAssignUserAvatarId(), 16);
                            Img img = new Img(genericTask.getAssignUserFullName(), avatarLink).setCSSClass(UIConstants.CIRCLE_BOX)
                                    .setTitle(genericTask.getAssignUserFullName());
                            rowComp.with(ELabel.html(img.write()).withWidthUndefined());

                            rowComp.with(toggleGenericTaskSummaryField).expand(toggleGenericTaskSummaryField);
                            issueLayout.addComponent(rowComp);

                        }
                    } else {
                        viewIssuesBtn.setCaption(AppContext.getMessage(ProjectI18nEnum.ACTION_VIEW_ASSIGNMENTS));
                        issueLayout.removeAllComponents();
                        issueLayout.setVisible(false);
                    }
                });
                viewIssuesBtn.addStyleName(WebUIConstants.BUTTON_LINK);
                progressLayout.with(viewIssuesBtn);
            }

            this.with(progressLayout, issueLayout);
        }
    }
}
