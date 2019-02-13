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
package com.mycollab.module.project.view.user;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.NumberUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTooltipGenerator;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IBeanList;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

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
            Button browseProjectsBtn = new Button(UserUIContext.getMessage(ProjectI18nEnum.ACTION_BROWSE),
                    clickEvent -> EventBusFactory.getInstance().post(new ProjectEvent.GotoList(this, null)));
            browseProjectsBtn.addStyleName(WebThemes.BUTTON_LINK);
            pageControls.addComponent(browseProjectsBtn, 0);
            pageControls.setComponentAlignment(browseProjectsBtn, Alignment.MIDDLE_LEFT);
        }
        return pageControls;
    }

    private static class ProjectRowDisplayHandler implements IBeanList.RowDisplayHandler<SimpleProject> {

        @Override
        public Component generateRow(IBeanList<SimpleProject> host, SimpleProject project, int rowIndex) {
            MHorizontalLayout layout = new MHorizontalLayout().withMargin(false).withFullWidth().withStyleName(WebThemes.BORDER_LIST_ROW);
            layout.addComponent(ProjectAssetsUtil.projectLogoComp(project.getShortname(), project.getId(), project.getAvatarid(), 64));
            if (project.isArchived()) {
                layout.addStyleName("project-archived");
            }
            MVerticalLayout mainLayout = new MVerticalLayout().withMargin(false);

            A projectDiv = new A(ProjectLinkGenerator.generateProjectLink(project.getId())).appendText(project.getName());
            ELabel projectLbl = ELabel.h3(projectDiv.write()).withStyleName(WebThemes.TEXT_ELLIPSIS).withFullWidth();
            projectLbl.setDescription(ProjectTooltipGenerator.generateToolTipProject(UserUIContext.getUserLocale(),
                    AppUI.getDateFormat(), project, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()), ContentMode.HTML);

            mainLayout.addComponent(projectLbl);

            MHorizontalLayout metaInfo = new MHorizontalLayout().withFullWidth();
            metaInfo.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

            Div activeMembersDiv = new Div().appendText(VaadinIcons.USERS.getHtml() + " " + project.getNumActiveMembers())
                    .setTitle(UserUIContext.getMessage(ProjectMemberI18nEnum.OPT_ACTIVE_MEMBERS));
            Div createdTimeDiv = new Div().appendText(VaadinIcons.CLOCK.getHtml() + " " + UserUIContext
                    .formatPrettyTime(project.getCreatedtime())).setTitle(UserUIContext.getMessage(GenericI18Enum.FORM_CREATED_TIME));
            Div billableHoursDiv = new Div().appendText(VaadinIcons.MONEY.getHtml() + " " + NumberUtils.roundDouble(2, project.getTotalBillableHours())).
                    setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS));
            Div nonBillableHoursDiv = new Div().appendText(VaadinIcons.GIFT.getHtml() + " " + NumberUtils.roundDouble(2,
                    project.getTotalNonBillableHours())).setTitle(UserUIContext.getMessage(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS));
            Div metaDiv = new Div().appendChild(activeMembersDiv, DivLessFormatter.EMPTY_SPACE, createdTimeDiv,
                    DivLessFormatter.EMPTY_SPACE, billableHoursDiv, DivLessFormatter.EMPTY_SPACE,
                    nonBillableHoursDiv, DivLessFormatter.EMPTY_SPACE);
            if (project.getMemlead() != null) {
                Div leadDiv = new Div().appendChild(new Img("", StorageUtils.getAvatarPath(project
                                .getLeadAvatarId(), 16)).setCSSClass(WebThemes.CIRCLE_BOX), DivLessFormatter.EMPTY_SPACE,
                        new A(ProjectLinkGenerator.generateProjectMemberLink(project.getId(), project.getMemlead()))
                                .appendText(StringUtils.trim(project.getLeadFullName(), 30, true))).setTitle
                        (UserUIContext.getMessage(ProjectI18nEnum.FORM_LEADER));
                metaDiv.appendChild(0, leadDiv);
                metaDiv.appendChild(1, DivLessFormatter.EMPTY_SPACE);
            }

            if (project.getClientid() != null) {
                Div accountDiv = new Div();
                if (project.getClientAvatarId() == null) {
                    accountDiv.appendText(VaadinIcons.INSTITUTION.getHtml() + " ");
                } else {
                    Img clientImg = new Img("", StorageUtils.getEntityLogoPath(AppUI.getAccountId(),
                            project.getClientAvatarId(), 16)).setCSSClass(WebThemes.CIRCLE_BOX);
                    accountDiv.appendChild(clientImg).appendChild(DivLessFormatter.EMPTY_SPACE);
                }

                accountDiv.appendChild(new A(ProjectLinkGenerator.generateClientPreviewLink(project.getClientid()))
                        .appendText(StringUtils.trim(project.getClientName(), 30, true))).setCSSClass(WebThemes.BLOCK)
                        .setTitle(project.getClientName());
                metaDiv.appendChild(0, accountDiv);
                metaDiv.appendChild(1, DivLessFormatter.EMPTY_SPACE);
            }
            metaDiv.setCSSClass(WebThemes.FLEX_DISPLAY);
            metaInfo.addComponent(ELabel.html(metaDiv.write()).withStyleName(WebThemes.META_INFO).withUndefinedWidth());

            mainLayout.addComponent(metaInfo);

            int openAssignments = project.getNumOpenBugs() + project.getNumOpenTasks() + project.getNumOpenRisks();
            int totalAssignments = project.getNumBugs() + project.getNumTasks() + project.getNumRisks();
            ELabel progressInfoLbl;
            if (totalAssignments > 0) {
                progressInfoLbl = new ELabel(UserUIContext.getMessage(ProjectI18nEnum.OPT_PROJECT_TICKET,
                        (totalAssignments - openAssignments), totalAssignments, (totalAssignments - openAssignments)
                                * 100 / totalAssignments)).withStyleName(WebThemes.META_INFO);
            } else {
                progressInfoLbl = new ELabel(UserUIContext.getMessage(ProjectI18nEnum.OPT_NO_TICKET))
                        .withStyleName(WebThemes.META_INFO);
            }
            mainLayout.addComponent(progressInfoLbl);
            layout.with(mainLayout).expand(mainLayout);
            return layout;
        }
    }
}
