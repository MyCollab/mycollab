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
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.reporting.ReportStreamSource;
import com.esofthead.mycollab.reporting.RpFieldsBuilder;
import com.esofthead.mycollab.reporting.SimpleReportTemplateExecutor;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.view.AbstractLazyPageView;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.ToggleButtonGroup;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.vaadin.floatingcomponent.FloatingComponent;
import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.data.Property;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.peter.buttongroup.ButtonGroup;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.*;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
@ViewComponent
public class MilestoneRoadmapViewImpl extends AbstractLazyPageView implements MilestoneRoadmapView {
    private Button createBtn;

    private MilestoneService milestoneService = ApplicationContextUtil.getSpringBean(MilestoneService.class);

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
        createBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES));

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

        closeMilestoneSelection.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                displayMilestones(tmpCriteria, closeMilestoneSelection.getValue(), inProgressMilestoneSelection
                        .getValue(), futureMilestoneSelection.getValue());
            }
        });
        inProgressMilestoneSelection.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                displayMilestones(tmpCriteria, closeMilestoneSelection.getValue(), inProgressMilestoneSelection
                        .getValue(), futureMilestoneSelection.getValue());
            }
        });
        futureMilestoneSelection.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                displayMilestones(tmpCriteria, closeMilestoneSelection.getValue(), inProgressMilestoneSelection
                        .getValue(), futureMilestoneSelection.getValue());
            }
        });
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
        List<SimpleMilestone> milestones = milestoneService.findPagableListByCriteria(new SearchRequest<>(baseCriteria, 0,
                Integer.MAX_VALUE));
        for (SimpleMilestone milestone : milestones) {
            roadMapView.addComponent(new MilestoneBlock(milestone));
        }

        headerText.setValue(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml() + " Roadmap ("
                + milestones.size() + ")");
    }

    private void initUI() {
        headerText = ELabel.h2("");

        MHorizontalLayout header = new MHorizontalLayout().withStyleName("hdr-view").withWidth("100%").withMargin(true)
                .with(headerText, createHeaderRight())
                .withAlign(headerText, Alignment.MIDDLE_LEFT).expand(headerText);
        this.addComponent(header);
        roadMapView = new MVerticalLayout().withSpacing(false);
        filterPanel = new MVerticalLayout().withWidth("250px").withStyleName("box");
        FloatingComponent floatingComponent = FloatingComponent.floatThis(filterPanel);
        floatingComponent.setContainerId("main-body");
        this.addComponent(new MHorizontalLayout().withFullWidth().with(roadMapView, filterPanel).expand(roadMapView));
    }

    private HorizontalLayout createHeaderRight() {
        MHorizontalLayout layout = new MHorizontalLayout();

        createBtn = new Button(AppContext.getMessage(MilestoneI18nEnum.BUTTON_NEW_PHASE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final Button.ClickEvent event) {
                SimpleMilestone milestone = new SimpleMilestone();
                milestone.setSaccountid(AppContext.getAccountId());
                milestone.setProjectid(CurrentProjectVariables.getProjectId());
                UI.getCurrent().addWindow(new MilestoneAddWindow(milestone));
            }
        });
        createBtn.setIcon(FontAwesome.PLUS);
        createBtn.setStyleName(UIConstants.BUTTON_ACTION);
        createBtn.setEnabled(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES));
        layout.with(createBtn);

        MButton exportPdfBtn = new MButton("").withIcon(FontAwesome.FILE_PDF_O).withStyleName(UIConstants.BUTTON_OPTION)
                .withDescription("Export to PDF");
        FileDownloader pdfFileDownloader = new FileDownloader(buildStreamSource(ReportExportType.PDF));
        pdfFileDownloader.extend(exportPdfBtn);

        MButton exportExcelBtn = new MButton("").withIcon(FontAwesome.FILE_EXCEL_O).withStyleName(UIConstants.BUTTON_OPTION).withDescription("Export to Excel");
        FileDownloader excelFileDownloader = new FileDownloader(buildStreamSource(ReportExportType.EXCEL));
        excelFileDownloader.extend(exportExcelBtn);

        MButton exportCsvBtn = new MButton("").withIcon(FontAwesome.FILE_TEXT_O).withStyleName(UIConstants
                .BUTTON_OPTION).withDescription("Export to Csv");
        FileDownloader csvFileDownloader = new FileDownloader(buildStreamSource(ReportExportType.CSV));
        csvFileDownloader.extend(exportCsvBtn);

        ButtonGroup exportButtonGroup = new ButtonGroup();
        exportButtonGroup.addButton(exportPdfBtn);
        exportButtonGroup.addButton(exportExcelBtn);
        exportButtonGroup.addButton(exportCsvBtn);

        layout.with(exportButtonGroup);

        Button kanbanBtn = new Button("Board", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new MilestoneEvent.GotoList(MilestoneRoadmapViewImpl.this, null));
            }
        });
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

    private StreamResource buildStreamSource(ReportExportType exportType) {
        List fields = Arrays.asList(MilestoneTableFieldDef.milestonename(), MilestoneTableFieldDef.status(),
                MilestoneTableFieldDef.startdate(), MilestoneTableFieldDef.enddate(), MilestoneTableFieldDef.id(),
                MilestoneTableFieldDef.assignee());
        SimpleReportTemplateExecutor reportTemplateExecutor = new SimpleReportTemplateExecutor.AllItems<>("Milestones",
                new RpFieldsBuilder(fields), exportType, SimpleMilestone.class, ApplicationContextUtil.getSpringBean
                (MilestoneService.class));
        ReportStreamSource streamSource = new ReportStreamSource(reportTemplateExecutor) {
            @Override
            protected Map<String, Object> initReportParameters() {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("siteUrl", AppContext.getSiteUrl());
                parameters.put(SimpleReportTemplateExecutor.CRITERIA, baseCriteria);
                return parameters;
            }
        };
        return new StreamResource(streamSource, exportType.getDefaultFileName());
    }

    private static class MilestoneBlock extends MVerticalLayout {
        private boolean showIssues = false;

        MilestoneBlock(final SimpleMilestone milestone) {
            this.setMargin(new MarginInfo(true, false, true, false));
            this.setStyleName("roadmap-block");

            ToggleMilestoneSummaryField toggleMilestoneSummaryField = new ToggleMilestoneSummaryField(milestone);
            this.with(toggleMilestoneSummaryField).expand(toggleMilestoneSummaryField);

            CssLayout metaBlock = new CssLayout();
            MilestonePopupFieldFactory popupFieldFactory = ViewManager.getCacheComponent(MilestonePopupFieldFactory.class);
            metaBlock.addComponent(popupFieldFactory.createMilestoneAssigneePopupField(milestone, true));
            metaBlock.addComponent(popupFieldFactory.createStartDatePopupField(milestone));
            metaBlock.addComponent(popupFieldFactory.createEndDatePopupField(milestone));
            metaBlock.addComponent(popupFieldFactory.createBillableHoursPopupField(milestone));
            metaBlock.addComponent(popupFieldFactory.createNonBillableHoursPopupField(milestone));
            this.add(metaBlock);

            if (StringUtils.isNotBlank(milestone.getDescription())) {
                ELabel descriptionLbl = new ELabel(StringUtils.formatRichText(milestone.getDescription()),
                        ContentMode.HTML);
                this.addComponent(descriptionLbl);
            }

            MHorizontalLayout progressLayout = new MHorizontalLayout();
            progressLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
            int openAssignments = milestone.getNumOpenBugs() + milestone.getNumOpenTasks();
            int totalAssignments = milestone.getNumBugs() + milestone.getNumTasks();
            ELabel progressInfoLbl;
            if (totalAssignments > 0) {
                progressInfoLbl = new ELabel(String.format("%d of %d issue(s) resolved. Progress (%d%%)",
                        (totalAssignments - openAssignments), totalAssignments, (totalAssignments - openAssignments)
                                * 100 / totalAssignments)).withStyleName(UIConstants.LABEL_META_INFO);
            } else {
                progressInfoLbl = new ELabel("No issue").withStyleName(UIConstants.LABEL_META_INFO);
            }

            final MVerticalLayout issueLayout = new MVerticalLayout().withMargin(new MarginInfo(false, true, false, true));
            issueLayout.setVisible(false);

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
                            ToggleGenericTaskSummaryField toggleGenericTaskSummaryField = new ToggleGenericTaskSummaryField(genericTask);
                            MHorizontalLayout rowComp = new MHorizontalLayout();
                            rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);
                            rowComp.with(new ELabel(ProjectAssetsManager.getAsset(genericTask.getType()).getHtml(), ContentMode.HTML).withWidthUndefined());
                            String avatarLink = StorageFactory.getInstance().getAvatarPath(genericTask.getAssignUserAvatarId(), 16);
                            Img img = new Img(genericTask.getAssignUserFullName(), avatarLink).setTitle(genericTask
                                    .getAssignUserFullName());
                            rowComp.with(new ELabel(img.write(), ContentMode.HTML).withWidthUndefined());

                            rowComp.with(toggleGenericTaskSummaryField).expand(toggleGenericTaskSummaryField);
                            issueLayout.addComponent(rowComp);

                        }
                    } else {
                        viewIssuesBtn.setCaption("View issues");
                        issueLayout.removeAllComponents();
                        issueLayout.setVisible(false);
                    }
                }
            };
            viewIssuesBtn.addClickListener(viewIssuesListener);
            viewIssuesBtn.addStyleName(UIConstants.BUTTON_ACTION);
            viewIssuesBtn.addStyleName(ValoTheme.BUTTON_SMALL);
            progressLayout.with(progressInfoLbl, viewIssuesBtn);
            this.with(progressLayout, issueLayout);
        }
    }
}
