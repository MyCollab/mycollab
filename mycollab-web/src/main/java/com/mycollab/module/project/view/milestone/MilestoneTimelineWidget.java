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

import com.hp.gagawa.java.elements.*;
import com.mycollab.common.i18n.DayI18nEnum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.service.MilestoneService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.Depot;
import com.vaadin.ui.CheckBox;
import org.vaadin.viritin.layouts.MCssLayout;

import java.util.Collections;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.4
 */
public class MilestoneTimelineWidget extends Depot {
    private List<SimpleMilestone> milestones;

    public MilestoneTimelineWidget() {
        super(UserUIContext.getMessage(MilestoneI18nEnum.OPT_TIMELINE), new MCssLayout());
        this.setWidth("100%");
        this.addStyleName("tm-container");
    }

    public void display() {
        final CheckBox noDateSetMilestone = new CheckBox(UserUIContext.getMessage(DayI18nEnum.OPT_NO_DATE_SET));
        noDateSetMilestone.setValue(false);

        final CheckBox includeClosedMilestone = new CheckBox(UserUIContext.getMessage(MilestoneStatus.Closed));
        includeClosedMilestone.setValue(false);

        noDateSetMilestone.addValueChangeListener(valueChangeEvent -> displayTimelines(noDateSetMilestone.getValue(), includeClosedMilestone.getValue()));
        includeClosedMilestone.addValueChangeListener(valueChangeEvent -> displayTimelines(noDateSetMilestone.getValue(), includeClosedMilestone.getValue()));

        addHeaderElement(noDateSetMilestone);
        addHeaderElement(includeClosedMilestone);

        MilestoneSearchCriteria searchCriteria = new MilestoneSearchCriteria();
        searchCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        searchCriteria.setOrderFields(Collections.singletonList(new SearchCriteria.OrderField(Milestone.Field.enddate.name(), "ASC")));
        MilestoneService milestoneService = AppContextUtil.getSpringBean(MilestoneService.class);
        milestones = (List<SimpleMilestone>) milestoneService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));

        bodyContent.addStyleName("tm-wrapper");
        displayTimelines(false, false);
    }

    private void displayTimelines(boolean includeNoDateSet, boolean includeClosedMilestone) {
        bodyContent.removeAllComponents();
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
                timestampDiv.appendChild(new Span().setCSSClass("author").appendText((totalAssignments -
                        openAssignments) * 100 / totalAssignments + "%"));
            } else {
                timestampDiv.appendChild(new Span().setCSSClass("author").appendText("100%"));
            }

            if (milestone.getEnddate() == null) {
                timestampDiv.appendChild(new Span().setCSSClass("date").appendText(UserUIContext.getMessage(DayI18nEnum.OPT_NO_DATE_SET)));
            } else {
                if (milestone.isOverdue()) {
                    timestampDiv.appendChild(new Span().setCSSClass("date overdue").appendText(String.format("%s (%s)",
                            UserUIContext.formatDate(milestone.getEnddate()),
                            UserUIContext.getMessage(ProjectCommonI18nEnum.OPT_DUE_IN, UserUIContext.formatDuration(milestone.getEnddate())))));
                } else {
                    timestampDiv.appendChild(new Span().setCSSClass("date").appendText(UserUIContext.formatDate(milestone.getEnddate())));
                }
            }
            li.appendChild(timestampDiv);

            Div statusDiv = new Div();

            A milestoneDiv = new A(ProjectLinkGenerator.generateMilestonePreviewLink
                    (milestone.getProjectid(), milestone.getId())).appendText(ProjectAssetsManager.getAsset
                    (ProjectTypeConstants.MILESTONE).getHtml() + " " + StringUtils.trim(milestone.getName(), 30, true))
                    .setId("tag" + TooltipHelper.TOOLTIP_ID);
            milestoneDiv.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ProjectTypeConstants.MILESTONE,
                    milestone.getId() + ""));
            milestoneDiv.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

            statusDiv.setCSSClass("status").appendChild(milestoneDiv);
            li.appendChild(statusDiv);
            ul.appendChild(li);
        }

        bodyContent.addComponent(ELabel.html(ul.write()).withUndefinedWidth());
    }
}
