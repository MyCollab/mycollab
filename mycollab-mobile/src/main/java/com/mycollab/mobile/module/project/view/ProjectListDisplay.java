/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.mobile.ui.DefaultPagedBeanList;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.module.file.service.AbstractStorageService;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class ProjectListDisplay extends DefaultPagedBeanList<ProjectService, ProjectSearchCriteria, SimpleProject> {
    private static final long serialVersionUID = -3362055893248919249L;

    public ProjectListDisplay() {
        super(AppContextUtil.getSpringBean(ProjectService.class), new ProjectRowDisplayHandler());
    }

    private static class ProjectRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleProject> {

        @Override
        public Component generateRow(IBeanList<SimpleProject> host, final SimpleProject project, int rowIndex) {
            MVerticalLayout layout = new MVerticalLayout();
            A prjLink = new A(ProjectLinkGenerator.generateProjectLink(project.getId())).appendText(String.format
                    ("[%s] %s", project.getShortname(), project.getName()));
            layout.with(ELabel.html(prjLink.write()).withStyleName(UIConstants.TEXT_ELLIPSIS, MobileUIConstants.LARGE));

            MHorizontalLayout metaInfo = new MHorizontalLayout();
            metaInfo.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            Div activeMembersDiv = new Div().appendText(FontAwesome.USERS.getHtml() + " " + project.getNumActiveMembers())
                    .setTitle(UserUIContext.getMessage(ProjectMemberI18nEnum.OPT_ACTIVE_MEMBERS));

            Div createdTimeDiv = new Div().appendText(FontAwesome.CLOCK_O.getHtml() + " " + UserUIContext
                    .formatPrettyTime(project.getCreatedtime())).setTitle(UserUIContext.getMessage(GenericI18Enum.FORM_CREATED_TIME));

            Div billableHoursDiv = new Div().appendText(FontAwesome.MONEY.getHtml() + " " + NumberUtils.roundDouble
                    (2, project.getTotalBillableHours())).setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS));

            Div nonBillableHoursDiv = new Div().appendText(FontAwesome.GIFT.getHtml() + " " + NumberUtils.roundDouble(2, project.getTotalNonBillableHours()));
            nonBillableHoursDiv.setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS));

            Div metaDiv = new Div().appendChild(activeMembersDiv, DivLessFormatter.EMPTY_SPACE, createdTimeDiv, DivLessFormatter.EMPTY_SPACE,
                    billableHoursDiv, DivLessFormatter.EMPTY_SPACE, nonBillableHoursDiv);
            if (project.getMemlead() != null) {
                Div leadDiv = new Div().appendChild(new Img("",
                                AppContextUtil.getSpringBean(AbstractStorageService.class)
                                        .getAvatarPath(project.getLeadAvatarId(), 16)).setCSSClass(UIConstants.CIRCLE_BOX),
                        new A(ProjectLinkGenerator.generateProjectMemberLink(project.getId(), project.getMemlead()))
                                .appendText(project.getLeadFullName())).setTitle(UserUIContext.getMessage(ProjectI18nEnum.FORM_LEADER));
                metaDiv.appendChild(0, leadDiv);
                metaDiv.appendChild(1, DivLessFormatter.EMPTY_SPACE);
            }
            metaDiv.setCSSClass("flex");
            metaInfo.addComponent(ELabel.html(metaDiv.write()).withStyleName(UIConstants.META_INFO));
            layout.addComponent(metaInfo);

            int openAssignments = project.getNumOpenBugs() + project.getNumOpenTasks() + project.getNumOpenRisks();
            int totalAssignments = project.getNumBugs() + project.getNumTasks() + project.getNumRisks();
            ELabel progressInfoLbl;
            if (totalAssignments > 0) {
                progressInfoLbl = new ELabel(UserUIContext.getMessage(ProjectI18nEnum.OPT_PROJECT_TICKET,
                        (totalAssignments - openAssignments), totalAssignments, (totalAssignments - openAssignments)
                                * 100 / totalAssignments)).withStyleName(UIConstants.META_INFO);
            } else {
                progressInfoLbl = new ELabel(UserUIContext.getMessage(ProjectI18nEnum.OPT_NO_TICKET))
                        .withStyleName(UIConstants.META_INFO);
            }
            layout.addComponent(progressInfoLbl);
            return layout;
        }
    }
}
