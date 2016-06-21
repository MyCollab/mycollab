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
package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.vaadin.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.AbstractBeanPagedList;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import static com.esofthead.mycollab.vaadin.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd
 * @since 5.2.4
 */
public class GenericTaskRowDisplayHandler implements AbstractBeanPagedList.RowDisplayHandler<ProjectGenericTask> {
    @Override
    public Component generateRow(AbstractBeanPagedList host, ProjectGenericTask genericTask, int rowIndex) {
        MHorizontalLayout rowComp = new MHorizontalLayout().withStyleName("list-row").withFullWidth();
        rowComp.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        Div issueDiv = new Div();
        issueDiv.appendText(ProjectAssetsManager.getAsset(genericTask.getType()).getHtml());

        String status = "";
        if (genericTask.isBug()) {
            status = AppContext.getMessage(OptionI18nEnum.BugStatus.class, genericTask.getStatus());
        } else if (genericTask.isMilestone()) {
            status = AppContext.getMessage(OptionI18nEnum.MilestoneStatus.class, genericTask.getStatus());
        } else if (genericTask.isRisk()) {
            status = AppContext.getMessage(com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.class,
                    genericTask.getStatus());
        } else if (genericTask.isTask()) {
            status = AppContext.getMessage(com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.class, genericTask.getStatus());
        }
        issueDiv.appendChild(new Span().appendText(status).setCSSClass(UIConstants.FIELD_NOTE));

        String avatarLink = StorageFactory.getInstance().getAvatarPath(genericTask.getAssignUserAvatarId(), 16);
        Img img = new Img(genericTask.getAssignUserFullName(), avatarLink).setTitle(genericTask
                .getAssignUserFullName());
        issueDiv.appendChild(img, DivLessFormatter.EMPTY_SPACE());

        A taskLink = new A().setId("tag" + TOOLTIP_ID);
        taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(genericTask.getType(), genericTask.getTypeId() + ""));
        taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
        if (ProjectTypeConstants.BUG.equals(genericTask.getType()) || ProjectTypeConstants.TASK.equals(genericTask.getType())) {
            taskLink.appendText(String.format("[%s-%d] - %s", genericTask.getProjectShortName(), genericTask.getExtraTypeId(),
                    genericTask.getName()));
            taskLink.setHref(ProjectLinkBuilder.generateProjectItemLink(genericTask.getProjectShortName(),
                    genericTask.getProjectId(), genericTask.getType(), genericTask.getExtraTypeId() + ""));
        } else {
            taskLink.appendText(genericTask.getName());
            taskLink.setHref(ProjectLinkBuilder.generateProjectItemLink(genericTask.getProjectShortName(),
                    genericTask.getProjectId(), genericTask.getType(), genericTask.getTypeId() + ""));
        }

        issueDiv.appendChild(taskLink);
        if (genericTask.isClosed()) {
            taskLink.setCSSClass("completed");
        } else if (genericTask.isOverdue()) {
            taskLink.setCSSClass("overdue");
            issueDiv.appendChild(new Span().appendText(" - Due in " + AppContext.formatDuration(genericTask.getDueDate()))
                    .setCSSClass(UIConstants.META_INFO));
        }

        MHorizontalLayout iconsLayout = new MHorizontalLayout().with(new ELabel(ProjectAssetsManager.getAsset
                (genericTask.getType()).getHtml(), ContentMode.HTML), new ELabel(img.write(), ContentMode.HTML));
        Label issueLbl = new Label(issueDiv.write(), ContentMode.HTML);
        rowComp.with(issueLbl);
        return rowComp;
    }
}
