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

import com.esofthead.mycollab.configuration.Storage;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTooltipGenerator;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.view.bug.BugDisplayWidget;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.LabelHTMLDisplayWidget;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DueBugWidget extends BugDisplayWidget {
    private static final long serialVersionUID = 1L;

    public DueBugWidget() {
        super(AppContext.getMessage(BugI18nEnum.WIDGET_DUE_BUGS_TITLE), true, DueBugRowDisplayHandler.class);
        this.setMargin(new MarginInfo(false, false, true, false));
    }

    public static class DueBugRowDisplayHandler extends BeanList.RowDisplayHandler<SimpleBug> {
        private static final long serialVersionUID = 1L;

        @Override
        public Component generateRow(SimpleBug bug, int rowIndex) {
            MVerticalLayout rowContent = new MVerticalLayout().withWidth("100%").withStyleName("list-row");
            Label defectLink = new Label(buildBugLink(bug), ContentMode.HTML);
            defectLink.addStyleName("font-large");
            defectLink.setDescription(ProjectTooltipGenerator.generateToolTipBug(AppContext.getUserLocale(), bug,
                    AppContext.getSiteUrl(), AppContext.getTimezone()));

            if (bug.isOverdue()) {
                defectLink.addStyleName(UIConstants.LINK_OVERDUE);
            }

            rowContent.addComponent(defectLink);

            LabelHTMLDisplayWidget descInfo = new LabelHTMLDisplayWidget(bug.getDescription());
            descInfo.setWidth("100%");
            rowContent.addComponent(descInfo);

            Div footer = new Div().setStyle("width:100%").setCSSClass("footer2");

            if (bug.getNumComments() != null && bug.getNumComments() > 0) {
                Div commetDiv = new Div().appendText(FontAwesome.COMMENT_O.getHtml() + " " + bug.getNumComments()).setTitle("Comments");
                footer.appendChild(commetDiv, DivLessFormatter.EMPTY_SPACE());
            }

            if (bug.getStatus() != null) {
                Div statusDiv = new Div().appendText(FontAwesome.INFO_CIRCLE.getHtml() + " " + AppContext.getMessage(OptionI18nEnum.BugStatus
                        .class, bug.getStatus())).setTitle(AppContext.getMessage(BugI18nEnum.FORM_STATUS));
                footer.appendChild(statusDiv).appendChild(DivLessFormatter.EMPTY_SPACE());
            }

            if (bug.getMilestoneid() != null) {
                Div milestoneDiv = new Div().appendText(ProjectAssetsManager.getAsset(ProjectTypeConstants.MILESTONE).getHtml() +
                        " " + StringUtils.trim(bug.getMilestoneName(), 20, true)).setTitle(bug.getMilestoneName());
                footer.appendChild(milestoneDiv).appendChild(DivLessFormatter.EMPTY_SPACE());
            }

            if (bug.getDuedate() != null) {
                String deadlineTooltip = String.format("%s: %s", AppContext.getMessage(BugI18nEnum.FORM_DUE_DATE),
                        AppContext.formatDate(bug.getDuedate()));
                Div dueDateDiv = new Div().appendText(VaadinIcons.USER_CLOCK.getHtml() + " " + AppContext
                        .formatPrettyTime(bug.getDueDateRoundPlusOne())).setTitle(deadlineTooltip);
                footer.appendChild(dueDateDiv).appendChild(DivLessFormatter.EMPTY_SPACE());
            }

            if (footer.getChildren().size() > 0) {
                rowContent.addComponent(new Label(footer.write(), ContentMode.HTML));
            }
            return rowContent;
        }

        private String buildBugLink(SimpleBug bug) {
            String uid = UUID.randomUUID().toString();
            String priority = bug.getPriority();
            Span priorityLink = new Span().appendText(ProjectAssetsManager.getBugPriorityHtml(priority)).setTitle(priority);

            String linkName = String.format("[#%d] - %s", bug.getBugkey(), bug.getSummary());
            A taskLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateBugPreviewFullLink(bug.getBugkey(),
                    CurrentProjectVariables.getShortName())).appendText(linkName).setStyle("display:inline");

            taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, ProjectTypeConstants.BUG,
                    bug.getId() + ""));
            taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));

            String avatarLink = Storage.getAvatarPath(bug.getAssignUserAvatarId(), 16);
            Img avatarImg = new Img(bug.getAssignuserFullName(), avatarLink).setTitle(bug.getAssignuserFullName());

            Div resultDiv = new DivLessFormatter().appendChild(priorityLink, DivLessFormatter.EMPTY_SPACE(),
                    avatarImg, DivLessFormatter.EMPTY_SPACE(), taskLink, DivLessFormatter.EMPTY_SPACE(),
                    TooltipHelper.buildDivTooltipEnable(uid));
            return resultDiv.write();
        }
    }
}
