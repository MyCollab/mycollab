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
package com.mycollab.module.project.view.user;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectTooltipGenerator;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.project.events.ProjectEvent;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.AbstractBeanPagedList;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class ProjectPagedList extends DefaultBeanPagedList<ProjectService, ProjectSearchCriteria, SimpleProject> {
    private static final long serialVersionUID = 1L;

    public ProjectPagedList() {
        super(AppContextUtil.getSpringBean(ProjectService.class), new ProjectRowDisplayHandler(), 4);
    }

    @Override
    protected MHorizontalLayout createPageControls() {
        MHorizontalLayout pageControls = super.createPageControls();
        if (pageControls != null) {
            Button browseProjectsBtn = new Button(AppContext.getMessage(ProjectI18nEnum.ACTION_BROWSE),
                    clickEvent -> EventBusFactory.getInstance().post(new ProjectEvent.GotoList(this, null)));
            browseProjectsBtn.addStyleName(UIConstants.BUTTON_LINK);
            pageControls.addComponent(browseProjectsBtn, 0);
            pageControls.setComponentAlignment(browseProjectsBtn, Alignment.MIDDLE_LEFT);
        }
        return pageControls;
    }

    public static class ProjectRowDisplayHandler implements AbstractBeanPagedList.RowDisplayHandler<SimpleProject> {

        @Override
        public Component generateRow(AbstractBeanPagedList host, final SimpleProject project, final int rowIndex) {
            final MHorizontalLayout layout = new MHorizontalLayout().withFullWidth().withStyleName("projectblock");
            layout.addComponent(ProjectAssetsUtil.buildProjectLogo(project.getShortname(), project.getId(), project.getAvatarid(), 64));
            if (project.isArchived()) {
                layout.addStyleName("projectlink-wrapper-archived");
            }
            final VerticalLayout linkIconFix = new VerticalLayout();
            linkIconFix.setSpacing(true);

            A projectDiv = new A(ProjectLinkBuilder.generateProjectFullLink(project.getId())).appendText(project.getName());
            ELabel projectLbl = ELabel.h3(projectDiv.write()).withStyleName(UIConstants.TEXT_ELLIPSIS).withFullWidth();
            projectLbl.setDescription(ProjectTooltipGenerator.generateToolTipProject(AppContext.getUserLocale(),
                    AppContext.getDateFormat(), project, AppContext.getSiteUrl(), AppContext.getUserTimeZone()));

            linkIconFix.addComponent(projectLbl);

            MHorizontalLayout metaInfo = new MHorizontalLayout().withFullWidth();
            metaInfo.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            Div activeMembersDiv = new Div().appendText(FontAwesome.USERS.getHtml() + " " + project.getNumActiveMembers()).setTitle("Active members");
            Div createdTimeDiv = new Div().appendText(FontAwesome.CLOCK_O.getHtml() + " " + AppContext
                    .formatPrettyTime(project.getCreatedtime())).setTitle(AppContext.getMessage(GenericI18Enum.FORM_CREATED_TIME));
            Div billableHoursDiv = new Div().appendText(FontAwesome.MONEY.getHtml() + " " + NumberUtils.roundDouble(2, project.getTotalBillableHours())).
                    setTitle(AppContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS));
            Div nonBillableHoursDiv = new Div().appendText(FontAwesome.GIFT.getHtml() + " " + NumberUtils.roundDouble(2,
                    project.getTotalNonBillableHours())).setTitle(AppContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS));
            Div metaDiv = new Div().appendChild(activeMembersDiv, DivLessFormatter.EMPTY_SPACE(), createdTimeDiv,
                    DivLessFormatter.EMPTY_SPACE(), billableHoursDiv, DivLessFormatter.EMPTY_SPACE(),
                    nonBillableHoursDiv, DivLessFormatter.EMPTY_SPACE());
            if (project.getLead() != null) {
                Div leadDiv = new Div().appendChild(new Img("", StorageFactory.getAvatarPath(project
                                .getLeadAvatarId(), 16)).setCSSClass(UIConstants.CIRCLE_BOX), DivLessFormatter.EMPTY_SPACE(),
                        new A(ProjectLinkBuilder.generateProjectMemberFullLink(project.getId(), project.getLead()))
                                .appendText(StringUtils.trim(project.getLeadFullName(), 30, true))).setTitle("Manager");
                metaDiv.appendChild(0, leadDiv);
                metaDiv.appendChild(1, DivLessFormatter.EMPTY_SPACE());
            }

            if (project.getAccountid() != null) {
                Div accountDiv = new Div();
                if (project.getClientAvatarId() == null) {
                    accountDiv.appendText(FontAwesome.INSTITUTION.getHtml() + " ");
                } else {
                    Img clientImg = new Img("", StorageFactory.getEntityLogoPath(AppContext
                            .getAccountId(), project.getClientAvatarId(), 16)).setCSSClass(UIConstants.CIRCLE_BOX);
                    accountDiv.appendChild(clientImg).appendChild(DivLessFormatter.EMPTY_SPACE());
                }

                accountDiv.appendChild(new A(ProjectLinkBuilder.generateClientPreviewFullLink(project.getAccountid()))
                        .appendText(StringUtils.trim(project.getClientName(), 30, true))).setCSSClass(UIConstants.BUTTON_BLOCK)
                        .setTitle(project.getClientName());
                metaDiv.appendChild(0, accountDiv);
                metaDiv.appendChild(1, DivLessFormatter.EMPTY_SPACE());
            }
            metaDiv.setCSSClass(UIConstants.FLEX_DISPLAY);
            metaInfo.addComponent(new ELabel(metaDiv.write(), ContentMode.HTML).withStyleName(UIConstants
                    .META_INFO).withWidthUndefined());

            linkIconFix.addComponent(metaInfo);

            int openAssignments = project.getNumOpenBugs() + project.getNumOpenTasks() + project.getNumOpenRisks();
            int totalAssignments = project.getNumBugs() + project.getNumTasks() + project.getNumRisks();
            ELabel progressInfoLbl;
            if (totalAssignments > 0) {
                progressInfoLbl = new ELabel(AppContext.getMessage(ProjectI18nEnum.OPT_PROJECT_ASSIGNMENT,
                        (totalAssignments - openAssignments), totalAssignments, (totalAssignments - openAssignments)
                                * 100 / totalAssignments)).withStyleName(UIConstants.META_INFO);
            } else {
                progressInfoLbl = new ELabel(AppContext.getMessage(ProjectI18nEnum.OPT_NO_ASSIGNMENT))
                        .withStyleName(UIConstants.META_INFO);
            }
            linkIconFix.addComponent(progressInfoLbl);
            layout.with(linkIconFix).expand(linkIconFix);
            return layout;
        }
    }
}
