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
package com.mycollab.module.project.ui.components;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.web.ui.table.DefaultPagedGrid;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
public class TicketTableDisplay extends DefaultPagedGrid<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> {
    private static final long serialVersionUID = 1L;

    public TicketTableDisplay(List<GridFieldMeta> displayColumns) {
        super(AppContextUtil.getSpringBean(ProjectTicketService.class), ProjectTicket.class, displayColumns);

//        addGeneratedColumn("name", (source, itemId, columnId) -> {
//            final ProjectTicket task = getBeanByIndex(itemId);
//
//            Div div = new DivLessFormatter();
//            Text image = new Text(ProjectAssetsManager.getAsset(task.getType()).getHtml());
//            A itemLink = new A().setId("tag" + TooltipHelper.TOOLTIP_ID);
//            if (ProjectTypeConstants.TASK.equals(task.getType()) || ProjectTypeConstants.BUG.equals(task.getType())) {
//                itemLink.setHref(ProjectLinkGenerator.generateProjectItemLink(task.getProjectShortName(),
//                        task.getProjectId(), task.getType(), task.getExtraTypeId() + ""));
//            } else {
//                itemLink.setHref(ProjectLinkGenerator.generateProjectItemLink(task.getProjectShortName(),
//                        task.getProjectId(), task.getType(), task.getTypeId() + ""));
//            }
//
//            itemLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(task.getType(), task.getTypeId() + ""));
//            itemLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
//            itemLink.appendText(task.getName());
//
//            div.appendChild(image, DivLessFormatter.EMPTY_SPACE, itemLink);
//
//            MButton assignmentLink = new MButton(div.write(),
//                    clickEvent -> fireTableEvent(new TableClickEvent(TicketTableDisplay.this, task, "name")))
//                    .withStyleName(WebThemes.BUTTON_LINK);
//            assignmentLink.setCaptionAsHtml(true);
//            return assignmentLink;
//        });
//
//        addGeneratedColumn("assignUser", (source, itemId, columnId) -> {
//            ProjectTicket task = getBeanByIndex(itemId);
//            return new ProjectMemberLink(task.getAssignUser(), task.getAssignUserAvatarId(), task.getAssignUserFullName());
//        });
//
//        addGeneratedColumn("dueDate", (source, itemId, columnId) -> {
//            ProjectTicket task = getBeanByIndex(itemId);
//            return new ELabel().prettyDate(task.getDueDate());
//        });
    }
}
