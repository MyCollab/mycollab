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
package com.esofthead.mycollab.module.project.view.bug.components;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTooltipGenerator;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.view.bug.BugDisplayWidget;
import com.esofthead.mycollab.module.project.view.parameters.BugFilterParameter;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.LabelHTMLDisplayWidget;
import com.esofthead.mycollab.vaadin.ui.LabelLink;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.hp.gagawa.java.elements.*;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DueBugWidget extends BugDisplayWidget {
    private static final long serialVersionUID = 1L;

    public DueBugWidget() {
        super(AppContext.getMessage(BugI18nEnum.WIDGET_DUE_BUGS_TITLE), true,
                DueBugRowDisplayHandler.class);
    }

    @Override
    protected BugFilterParameter constructMoreDisplayFilter() {
        return new BugFilterParameter("Due Bugs", searchCriteria);
    }

    public static class DueBugRowDisplayHandler extends BeanList.RowDisplayHandler<SimpleBug> {
        private static final long serialVersionUID = 1L;

        @Override
        public Component generateRow(SimpleBug bug, int rowIndex) {
            MVerticalLayout rowContent = new MVerticalLayout().withWidth("100%").withStyleName("list-row");
            LabelLink defectLink = new LabelLink(String.format("[#%d] - %s", bug.getBugkey(), bug.getSummary()),
                    ProjectLinkBuilder.generateBugPreviewFullLink(
                            bug.getBugkey(), bug.getProjectShortName()));
            defectLink.setIconLink(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG));
            defectLink.setWidth("100%");
            defectLink.addStyleName("font-large");
            defectLink.setDescription(ProjectTooltipGenerator
                    .generateToolTipBug(AppContext.getUserLocale(), bug,
                            AppContext.getSiteUrl(), AppContext.getTimezone()));

            if (bug.isOverdue()) {
                defectLink.addStyleName(UIConstants.LINK_OVERDUE);
            }

            rowContent.addComponent(defectLink);

            LabelHTMLDisplayWidget descInfo = new LabelHTMLDisplayWidget(bug.getDescription());
            descInfo.setWidth("100%");
            rowContent.addComponent(descInfo);

            String bugInfo = String.format("%s: %s - %s: %s - %s: ",
                    AppContext.getMessage(BugI18nEnum.FORM_DUE_DATE),
                    AppContext.formatPrettyTime(bug.getDuedate()),
                    AppContext.getMessage(BugI18nEnum.FORM_STATUS),
                    AppContext.getMessage(BugStatus.class, bug.getStatus()), AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE));
            Div footer = new Div().setStyle("width:100%").setCSSClass("footer2");
            Span bugInfoTxt = new Span().appendText(bugInfo).setTitle(AppContext.formatDate
                    (bug.getDuedate()));
            if (StringUtils.isBlank(bug.getAssignuser())) {
                footer.appendChild(bugInfoTxt, DivLessFormatter.EMPTY_SPACE(), new Text("None"));
            } else {
                String uid = UUID.randomUUID().toString();
                Img userAvatar = new Img("", StorageManager.getAvatarLink(bug.getAssignUserAvatarId(), 16));
                A userLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateProjectMemberFullLink(bug
                        .getProjectid(), bug.getAssignuser())).appendText(bug.getAssignuserFullName());
                userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsDunction(uid, bug.getAssignuser()));
                userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
                footer.appendChild(bugInfoTxt, DivLessFormatter.EMPTY_SPACE(), userAvatar, DivLessFormatter.EMPTY_SPACE()
                        , userLink, DivLessFormatter.EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid));
            }

            rowContent.add(new Label(footer.write(), ContentMode.HTML));
            return rowContent;
        }
    }
}
