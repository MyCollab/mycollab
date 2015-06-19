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

import com.esofthead.mycollab.common.i18n.DayI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTooltipGenerator;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
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
 * @since 5.0.0
 */
public class BugRowDisplayHandler extends BeanList.RowDisplayHandler<SimpleBug> {
    @Override
    public Component generateRow(SimpleBug bug, int rowIndex) {
        MVerticalLayout rowContent = new MVerticalLayout().withWidth("100%").withStyleName("list-row");
        LabelLink defectLink = new LabelLink(String.format("[#%d] - %s", bug.getBugkey(), bug.getSummary()),
                ProjectLinkBuilder.generateBugPreviewFullLink(bug.getBugkey(), bug.getProjectShortName()));
        defectLink.setWidth("100%");
        defectLink.addStyleName("font-large");
        defectLink.setIconLink(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG));
        defectLink.setDescription(ProjectTooltipGenerator
                .generateToolTipBug(AppContext.getUserLocale(), bug,
                        AppContext.getSiteUrl(), AppContext.getTimezone()));

        if (bug.isCompleted()) {
            defectLink.addStyleName(UIConstants.LINK_COMPLETED);
        } else if (bug.isOverdue()) {
            defectLink.addStyleName(UIConstants.LINK_OVERDUE);
        }
        rowContent.addComponent(defectLink);

        LabelHTMLDisplayWidget descInfo = new LabelHTMLDisplayWidget(bug.getDescription());
        descInfo.setWidth("100%");
        rowContent.addComponent(descInfo);

        Div footer = new Div().setStyle("width:100%").setCSSClass("footer2");
        Span lastUpdatedTimeTxt = new Span().appendText(AppContext.getMessage(
                DayI18nEnum.LAST_UPDATED_ON,
                AppContext.formatPrettyTime(bug.getLastupdatedtime()))).setTitle(AppContext.formatDateTime(bug
                .getLastupdatedtime()));

        Text assigneeTxt = new Text("&nbsp;-&nbsp;" + AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE) + ":&nbsp;");
        if (StringUtils.isBlank(bug.getAssignuser())) {
            footer.appendChild(lastUpdatedTimeTxt, assigneeTxt, new Text("None"));
        } else {
            String uid = UUID.randomUUID().toString();
            Img userAvatar = new Img("", StorageManager.getAvatarLink(bug.getAssignUserAvatarId(), 16));
            A userLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateProjectMemberFullLink(bug
                    .getProjectid(), bug.getAssignuser())).appendText(com.esofthead.mycollab.core.utils.StringUtils
                    .trim(bug.getAssignuserFullName(), 30, true));
            userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(uid, bug.getAssignuser()));
            userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
            footer.appendChild(lastUpdatedTimeTxt, assigneeTxt, userAvatar, DivLessFormatter.EMPTY_SPACE(), userLink,
                    DivLessFormatter.EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid));
        }

        rowContent.addComponent(new Label(footer.write(), ContentMode.HTML));
        return rowContent;
    }
}
