/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.core.utils.NumberUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class ProjectListDisplay extends DefaultPagedBeanList<ProjectService, ProjectSearchCriteria, SimpleProject> {
    private static final long serialVersionUID = -3362055893248919249L;

    public ProjectListDisplay() {
        super(ApplicationContextUtil.getSpringBean(ProjectService.class), new ProjectRowDisplayHandler());
    }

    public static class ProjectRowDisplayHandler implements RowDisplayHandler<SimpleProject> {

        @Override
        public Component generateRow(final SimpleProject project, int rowIndex) {
            MVerticalLayout layout = new MVerticalLayout();
            A prjLink = new A(ProjectLinkBuilder.generateProjectFullLink(project.getId())).appendText(String.format
                    ("[%s] %s", project.getShortname(), project.getName()));
            layout.with(new ELabel(prjLink.write(), ContentMode.HTML).withStyleName(UIConstants.TRUNCATE));

            MHorizontalLayout metaInfo = new MHorizontalLayout();
            metaInfo.setDefaultComponentAlignment(Alignment.TOP_LEFT);

            Label projectMemberBtn = new ELabel(FontAwesome.USERS.getHtml() + " " + project.getNumActiveMembers(),
                    ContentMode.HTML).withDescription("Active members").withStyleName(UIConstants.META_INFO);

            metaInfo.addComponent(projectMemberBtn);
            Label createdTimeLbl = new ELabel(FontAwesome.CLOCK_O.getHtml() + " " + AppContext.formatPrettyTime(project.getCreatedtime()),
                    ContentMode.HTML).withDescription("Created time").withStyleName(UIConstants.META_INFO);
            metaInfo.addComponent(createdTimeLbl);

            Label billableHoursLbl = new ELabel(FontAwesome.MONEY.getHtml() + " " + NumberUtils.roundDouble(2, project.getTotalBillableHours()),
                    ContentMode.HTML).withDescription("Billable hours").withStyleName(UIConstants.META_INFO);
            metaInfo.addComponent(billableHoursLbl);

            Label nonBillableHoursLbl = new ELabel(FontAwesome.GIFT.getHtml() + " " + NumberUtils.roundDouble(2,
                    project.getTotalNonBillableHours()), ContentMode.HTML)
                    .withDescription("Non billable hours").withStyleName(UIConstants.META_INFO);
            metaInfo.addComponent(nonBillableHoursLbl);
            layout.addComponent(metaInfo);

            int openAssignments = project.getNumOpenBugs() + project.getNumOpenTasks() + project.getNumOpenRisks() + project.getNumOpenRisks();
            int totalAssignments = project.getNumBugs() + project.getNumTasks() + project.getNumRisks() + project.getNumProblems();
            ELabel progressInfoLbl;
            if (totalAssignments > 0) {
                progressInfoLbl = new ELabel(String.format("%d of %d issue(s) resolved. Progress (%d%%)",
                        (totalAssignments - openAssignments), totalAssignments, (totalAssignments - openAssignments)
                                * 100 / totalAssignments)).withStyleName(UIConstants.META_INFO);
            } else {
                progressInfoLbl = new ELabel("No issue").withStyleName(UIConstants.META_INFO);
            }
            layout.addComponent(progressInfoLbl);

            return layout;
        }
    }
}
