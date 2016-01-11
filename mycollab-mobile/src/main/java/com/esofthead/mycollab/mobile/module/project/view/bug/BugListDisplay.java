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
package com.esofthead.mycollab.mobile.module.project.view.bug;

import com.esofthead.mycollab.common.i18n.DayI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.hp.gagawa.java.elements.A;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class BugListDisplay extends DefaultPagedBeanList<BugService, BugSearchCriteria, SimpleBug> {
    private static final long serialVersionUID = -8911176517887730007L;

    public BugListDisplay() {
        super(ApplicationContextUtil.getSpringBean(BugService.class), new BugRowDisplayHandler());
    }

    private static class BugRowDisplayHandler implements RowDisplayHandler<SimpleBug> {

        @Override
        public Component generateRow(final SimpleBug bug, int rowIndex) {
            MVerticalLayout bugRowLayout = new MVerticalLayout().withWidth("100%");

            A bugLink = new A(ProjectLinkBuilder.generateBugPreviewFullLink(bug.getBugkey(), bug.getProjectShortName
                    ())).appendText(String.format("[#%s] - %s", bug.getBugkey(), bug.getSummary()));

            CssLayout bugLbl = new CssLayout(new ELabel(bugLink.write(), ContentMode.HTML).withStyleName(UIConstants.TRUNCATE));
            bugRowLayout.with(new MHorizontalLayout(new ELabel(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG)
                    .getHtml(), ContentMode.HTML).withWidthUndefined(), bugLbl).expand(bugLbl).withFullWidth());

            CssLayout metaInfoLayout = new CssLayout();
            bugRowLayout.with(metaInfoLayout);

            ELabel lastUpdatedTimeLbl = new ELabel(AppContext.getMessage(DayI18nEnum.LAST_UPDATED_ON, AppContext
                    .formatPrettyTime((bug.getLastupdatedtime())))).withStyleName(UIConstants.META_INFO);
            metaInfoLayout.addComponent(lastUpdatedTimeLbl);

            A assigneeLink = new A();
            assigneeLink.setHref(ProjectLinkGenerator.generateProjectMemberFullLink(AppContext.getSiteUrl(),
                    CurrentProjectVariables.getProjectId(), bug.getAssignuser()));
            assigneeLink.appendText(bug.getAssignuserFullName());

            ELabel assigneeLbl = new ELabel(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE) + (bug
                    .getAssignuserFullName() == null ?
                    ":&nbsp;N/A&nbsp;" : ":&nbsp;" + assigneeLink.write()), ContentMode.HTML).withStyleName(UIConstants.META_INFO);
            assigneeLbl.addStyleName(UIConstants.TRUNCATE);
            metaInfoLayout.addComponent(assigneeLbl);

            ELabel statusLbl = new ELabel(AppContext.getMessage(BugI18nEnum.FORM_STATUS) + ": " + AppContext.getMessage
                    (BugStatus.class, bug.getStatus()), ContentMode.HTML).withStyleName(UIConstants.META_INFO);
            metaInfoLayout.addComponent(statusLbl);

            return bugRowLayout;
        }

    }
}
