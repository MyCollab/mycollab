package com.mycollab.module.project.view.milestone;

import com.hp.gagawa.java.elements.*;
import com.mycollab.common.i18n.DayI18nEnum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.view.UserDashboardView;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Collections;
import java.util.List;

import static com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd
 * @since 5.2.4
 */
public class AllMilestoneTimelineWidget extends MVerticalLayout {
    private List<SimpleMilestone> milestones;
    private CssLayout timelineContainer;

    public void display() {
        this.withMargin(new MarginInfo(false, false, true, false)).withStyleName("tm-container").withFullWidth();

        MHorizontalLayout headerLayout = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, true))
                .withStyleName(WebThemes.PANEL_HEADER, "wrapped");
        headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        ELabel titleLbl = ELabel.h3(UserUIContext.getMessage(MilestoneI18nEnum.OPT_TIMELINE));

        final CheckBox includeNoDateSet = new CheckBox(UserUIContext.getMessage(DayI18nEnum.OPT_NO_DATE_SET));
        includeNoDateSet.setValue(false);

        final CheckBox includeClosedMilestone = new CheckBox(UserUIContext.getMessage(MilestoneStatus.Closed));
        includeClosedMilestone.setValue(false);

        includeNoDateSet.addValueChangeListener(valueChangeEvent -> displayTimelines(includeNoDateSet.getValue(), includeClosedMilestone.getValue()));
        includeClosedMilestone.addValueChangeListener(valueChangeEvent -> displayTimelines(includeNoDateSet.getValue(), includeClosedMilestone.getValue()));
        headerLayout.with(titleLbl, includeNoDateSet, includeClosedMilestone).expand(titleLbl).withAlign(includeNoDateSet, Alignment
                .MIDDLE_RIGHT).withAlign(includeClosedMilestone, Alignment.MIDDLE_RIGHT);

        MilestoneSearchCriteria searchCriteria = new MilestoneSearchCriteria();
        UserDashboardView userDashboardView = UIUtils.getRoot(this, UserDashboardView.class);
        searchCriteria.setProjectIds(new SetSearchField<>(userDashboardView.getInvolvedProjectKeys()));
        searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField(Milestone.Field.enddate.name(), "ASC")));
        MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
        milestones = (List<SimpleMilestone>) milestoneService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));

        this.addComponent(headerLayout);
        timelineContainer = new CssLayout();
        timelineContainer.setWidth("100%");
        this.addComponent(timelineContainer);
        timelineContainer.addStyleName("tm-wrapper");
        displayTimelines(false, false);
    }

    private void displayTimelines(boolean includeNoDateSet, boolean includeClosedMilestone) {
        timelineContainer.removeAllComponents();
        Ul ul = new Ul().setCSSClass("timeline");

        for (SimpleMilestone milestone : milestones) {
            if (!includeClosedMilestone) {
                if (MilestoneStatus.Closed.name().equals(milestone.getStatus())) {
                    continue;
                }
            }
            if (!includeNoDateSet) {
                if (milestone.getEnddate() == null) {
                    continue;
                }
            }
            Li li = new Li();
            if (MilestoneStatus.Closed.name().equals(milestone.getStatus())) {
                li.setCSSClass("li closed");
            } else if (MilestoneStatus.InProgress.name().equals(milestone.getStatus())) {
                li.setCSSClass("li inprogress");
            } else if (MilestoneStatus.Future.name().equals(milestone.getStatus())) {
                li.setCSSClass("li future");
            }

            Div timestampDiv = new Div().setCSSClass("timestamp");

            int openAssignments = milestone.getNumOpenBugs() + milestone.getNumOpenTasks() + milestone.getNumOpenRisks();
            int totalAssignments = milestone.getNumBugs() + milestone.getNumTasks() + milestone.getNumRisks();
            if (totalAssignments > 0) {
                timestampDiv.appendChild(new Span().appendText((totalAssignments -
                        openAssignments) * 100 / totalAssignments + "%"));
            } else {
                timestampDiv.appendChild(new Span().appendText("100%"));
            }

            if (milestone.getEnddate() == null) {
                timestampDiv.appendChild(new Span().setCSSClass("date").appendText(UserUIContext.getMessage(DayI18nEnum.OPT_NO_DATE_SET)));
            } else {
                if (milestone.isOverdue()) {
                    timestampDiv.appendChild(new Span().setCSSClass("date overdue").appendText(UserUIContext.formatDate(milestone.getEnddate()) +
                            " (" + UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_DUE_IN, UserUIContext.formatDuration(milestone.getEnddate())) + ")"));
                } else {
                    timestampDiv.appendChild(new Span().setCSSClass("date").appendText(UserUIContext.formatDate(milestone.getEnddate())));
                }
            }
            li.appendChild(timestampDiv);

            A projectDiv = new A(ProjectLinkBuilder.generateProjectFullLink(milestone.getProjectid())).appendText
                    (FontAwesome.BUILDING_O.getHtml() + " " + StringUtils.trim(milestone.getProjectName(), 30, true))
                    .setId("tag" + TooltipHelper.TOOLTIP_ID);
            projectDiv.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ProjectTypeConstants.PROJECT,
                    milestone.getProjectid() + ""));
            projectDiv.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

            A milestoneDiv = new A(ProjectLinkBuilder.generateMilestonePreviewFullLink
                    (milestone.getProjectid(), milestone.getId())).appendText(ProjectAssetsManager.getAsset
                    (ProjectTypeConstants.MILESTONE).getHtml() + " " + StringUtils.trim(milestone.getName(), 30, true))
                    .setId("tag" + TooltipHelper.TOOLTIP_ID);
            milestoneDiv.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ProjectTypeConstants.MILESTONE,
                    milestone.getId() + ""));
            milestoneDiv.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

            Div statusDiv = new Div().setCSSClass("status").appendChild(projectDiv, milestoneDiv);
            li.appendChild(statusDiv);
            ul.appendChild(li);
        }

        timelineContainer.addComponent(ELabel.html(ul.write()).withWidthUndefined());
    }
}
