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

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.query.LazyValueInjector;
import com.mycollab.eventmanager.ApplicationEventListener;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.event.MilestoneEvent;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.ComponentUtils;
import com.mycollab.module.project.view.service.MilestoneComponentFactory;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.view.AbstractLazyPageView;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.ToggleButtonGroup;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.web.CustomLayoutExt;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class MilestoneListViewImpl extends AbstractLazyPageView implements MilestoneListView {
    private static final long serialVersionUID = 1L;

    private MVerticalLayout inProgressContainer;
    private Label inProgressHeader;

    private MVerticalLayout futureContainer;
    private Label futureHeader;

    private MVerticalLayout closeContainer;
    private Label closedHeader;

    private MilestoneSearchCriteria baseCriteria;

    private ApplicationEventListener<MilestoneEvent.NewMilestoneAdded> newMilestoneHandler = new
            ApplicationEventListener<MilestoneEvent.NewMilestoneAdded>() {
                @Override
                @Subscribe
                public void handle(MilestoneEvent.NewMilestoneAdded event) {
                    displayMilestones();
                }
            };

    private ApplicationEventListener<MilestoneEvent.MilestoneDeleted> deletedMilestoneHandler = new
            ApplicationEventListener<MilestoneEvent.MilestoneDeleted>() {
                @Override
                @Subscribe
                public void handle(MilestoneEvent.MilestoneDeleted event) {
                    displayMilestones();
                }
            };

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
        constructBody();
        displayMilestones();
    }

    private void displayMilestones() {
        closeContainer.removeAllComponents();
        inProgressContainer.removeAllComponents();
        futureContainer.removeAllComponents();
        baseCriteria = new MilestoneSearchCriteria();
        baseCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
        List<SimpleMilestone> milestones = milestoneService.findAbsoluteListByCriteria(baseCriteria, 0, Integer.MAX_VALUE);
        int totalClosedMilestones = 0, totalInprogressMilestones = 0, totalFutureMilestones = 0;

        for (SimpleMilestone milestone : milestones) {
            ComponentContainer componentContainer = new MilestoneBox(milestone);
            if (MilestoneStatus.InProgress.name().equals(milestone.getStatus())) {
                inProgressContainer.addComponent(componentContainer);
                totalInprogressMilestones++;
            } else if (MilestoneStatus.Future.name().equals(milestone.getStatus())) {
                futureContainer.addComponent(componentContainer);
                totalFutureMilestones++;
            } else if (MilestoneStatus.Closed.name().equals(milestone.getStatus())) {
                closeContainer.addComponent(componentContainer);
                totalClosedMilestones++;
            }
        }

        updateClosedMilestoneNumber(totalClosedMilestones);
        updateFutureMilestoneNumber(totalFutureMilestones);
        updateInProgressMilestoneNumber(totalInprogressMilestones);
    }

    private void initUI() {
        HeaderWithFontAwesome headerText = ComponentUtils.headerH2(ProjectTypeConstants.MILESTONE,
                UserUIContext.getMessage(MilestoneI18nEnum.LIST));

        MHorizontalLayout header = new MHorizontalLayout().withStyleName("hdr-view").withFullWidth().withMargin(true)
                .with(headerText, createHeaderRight()).withAlign(headerText, Alignment.MIDDLE_LEFT).expand(headerText);
        this.addComponent(header);
    }

    private HorizontalLayout createHeaderRight() {
        MHorizontalLayout layout = new MHorizontalLayout();

        MButton createBtn = new MButton(UserUIContext.getMessage(MilestoneI18nEnum.NEW), clickEvent -> {
            SimpleMilestone milestone = new SimpleMilestone();
            milestone.setSaccountid(MyCollabUI.getAccountId());
            milestone.setProjectid(CurrentProjectVariables.getProjectId());
            UI.getCurrent().addWindow(new MilestoneAddWindow(milestone));
        }).withIcon(FontAwesome.PLUS).withStyleName(WebUIConstants.BUTTON_ACTION)
                .withVisible(CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.MILESTONES));
        layout.with(createBtn);

        MButton printBtn = new MButton("", clickEvent -> UI.getCurrent().addWindow(new
                MilestoneCustomizeReportOutputWindow(new LazyValueInjector() {
            @Override
            protected Object doEval() {
                return baseCriteria;
            }
        }))).withIcon(FontAwesome.PRINT).withStyleName(WebUIConstants.BUTTON_OPTION)
                .withDescription(UserUIContext.getMessage(GenericI18Enum.ACTION_EXPORT));
        layout.addComponent(printBtn);

        MButton boardBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_BOARD)).withIcon(FontAwesome.SERVER).withWidth("100px");

        MButton roadmapBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_LIST),
                clickEvent -> EventBusFactory.getInstance().post(new MilestoneEvent.GotoRoadmap(MilestoneListViewImpl.this)))
                .withIcon(FontAwesome.NAVICON).withWidth("100px");


        ToggleButtonGroup viewButtons = new ToggleButtonGroup();
        viewButtons.addButton(roadmapBtn);
        viewButtons.addButton(boardBtn);
        if (!SiteConfiguration.isCommunityEdition()) {
            MButton kanbanBtn = new MButton(UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_KANBAN),
                    clickEvent -> EventBusFactory.getInstance().post(new MilestoneEvent.GotoKanban(MilestoneListViewImpl.this)))
                    .withIcon(FontAwesome.TH).withWidth("100px");
            viewButtons.addButton(kanbanBtn);
        }
        viewButtons.withDefaultButton(boardBtn);
        layout.with(viewButtons);

        return layout;
    }

    private void constructBody() {
        CustomLayout bodyContent = CustomLayoutExt.createLayout("milestoneView");
        bodyContent.setWidth("100%");
        bodyContent.setStyleName("milestone-view");

        MHorizontalLayout closedHeaderLayout = new MHorizontalLayout();

        closedHeader = ELabel.html("").withWidthUndefined();
        closedHeaderLayout.with(closedHeader).withAlign(closedHeader, Alignment.MIDDLE_CENTER);

        bodyContent.addComponent(closedHeaderLayout, "closed-header");
        closeContainer = new MVerticalLayout().withStyleName("milestone-col").withFullWidth();
        bodyContent.addComponent(closeContainer, "closed-milestones");

        MHorizontalLayout inProgressHeaderLayout = new MHorizontalLayout();
        inProgressHeader = ELabel.html("").withWidthUndefined();
        inProgressHeaderLayout.addComponent(inProgressHeader);
        inProgressHeaderLayout.setComponentAlignment(inProgressHeader, Alignment.MIDDLE_CENTER);

        bodyContent.addComponent(inProgressHeaderLayout, "in-progress-header");
        inProgressContainer = new MVerticalLayout().withStyleName("milestone-col").withFullWidth();
        bodyContent.addComponent(this.inProgressContainer, "in-progress-milestones");

        MHorizontalLayout futureHeaderLayout = new MHorizontalLayout();
        futureHeader = ELabel.html("").withWidthUndefined();
        futureHeaderLayout.addComponent(futureHeader);
        futureHeaderLayout.setComponentAlignment(futureHeader, Alignment.MIDDLE_CENTER);

        bodyContent.addComponent(futureHeaderLayout, "future-header");
        futureContainer = new MVerticalLayout().withStyleName("milestone-col").withFullWidth();
        bodyContent.addComponent(this.futureContainer, "future-milestones");

        this.addComponent(bodyContent);
    }

    private void updateClosedMilestoneNumber(int closeMilestones) {
        closedHeader.setValue(ProjectAssetsManager.getMilestoneStatus(MilestoneStatus.Closed.name()).getHtml() + " " +
                UserUIContext.getMessage(MilestoneI18nEnum.WIDGET_CLOSED_PHASE_TITLE) + " (" + closeMilestones + ")");
    }

    private void updateFutureMilestoneNumber(int futureMilestones) {
        futureHeader.setValue(ProjectAssetsManager.getMilestoneStatus(MilestoneStatus.Future.name()).getHtml() + " " +
                UserUIContext.getMessage(MilestoneI18nEnum.WIDGET_FUTURE_PHASE_TITLE) + " (" + futureMilestones + ")");
    }

    private void updateInProgressMilestoneNumber(int inProgressMilestones) {
        inProgressHeader.setValue(ProjectAssetsManager.getMilestoneStatus(MilestoneStatus.InProgress.name()).getHtml() + " " +
                UserUIContext.getMessage(MilestoneI18nEnum.WIDGET_INPROGRESS_PHASE_TITLE) + " (" + inProgressMilestones +
                ")");
    }

    private class MilestoneBox extends CssLayout {
        MilestoneBox(final SimpleMilestone milestone) {
            this.addStyleName(WebUIConstants.MILESTONE_BOX);
            this.setWidth("100%");

            ToggleMilestoneSummaryField toggleMilestoneSummaryField = new ToggleMilestoneSummaryField(milestone, 50, false);

            MHorizontalLayout milestoneHeader = new MHorizontalLayout().withFullWidth()
                    .with(toggleMilestoneSummaryField).expand(toggleMilestoneSummaryField);

            this.addComponent(milestoneHeader);

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
            this.addComponent(progressInfoLbl);

            CssLayout metaBlock = new CssLayout();
            MilestoneComponentFactory popupFieldFactory = AppContextUtil.getSpringBean(MilestoneComponentFactory.class);
            metaBlock.addComponent(popupFieldFactory.createMilestoneAssigneePopupField(milestone, false));
            metaBlock.addComponent(popupFieldFactory.createStartDatePopupField(milestone));
            metaBlock.addComponent(popupFieldFactory.createEndDatePopupField(milestone));
            if (!SiteConfiguration.isCommunityEdition()) {
                metaBlock.addComponent(popupFieldFactory.createBillableHoursPopupField(milestone));
                metaBlock.addComponent(popupFieldFactory.createNonBillableHoursPopupField(milestone));
            }

            this.addComponent(metaBlock);
        }
    }
}
