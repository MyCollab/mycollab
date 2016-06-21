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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectGenericTaskService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.TooltipHelper;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

import java.util.List;

import static com.esofthead.mycollab.vaadin.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class GenericTaskTableDisplay extends DefaultPagedBeanTable<ProjectGenericTaskService, ProjectGenericTaskSearchCriteria, ProjectGenericTask> {
    private static final long serialVersionUID = 1L;

    public GenericTaskTableDisplay(List<TableViewField> displayColumns) {
        super(AppContextUtil.getSpringBean(ProjectGenericTaskService.class), ProjectGenericTask.class, displayColumns);

        addGeneratedColumn("name", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component generateCell(Table source, Object itemId, Object columnId) {
                final ProjectGenericTask task = getBeanByIndex(itemId);

                Div div = new DivLessFormatter();
                Text image = new Text(ProjectAssetsManager.getAsset(task.getType()).getHtml());
                A itemLink = new A().setId("tag" + TOOLTIP_ID);
                if (ProjectTypeConstants.TASK.equals(task.getType()) || ProjectTypeConstants.BUG.equals(task.getType())) {
                    itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(task.getProjectShortName(),
                            task.getProjectId(), task.getType(), task.getExtraTypeId() + ""));
                } else {
                    itemLink.setHref(ProjectLinkBuilder.generateProjectItemLink(task.getProjectShortName(),
                            task.getProjectId(), task.getType(), task.getTypeId() + ""));
                }

                itemLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(task.getType(), task.getTypeId() + ""));
                itemLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
                itemLink.appendText(task.getName());

                div.appendChild(image, DivLessFormatter.EMPTY_SPACE(), itemLink);

                Button assignmentLink = new Button(div.write(), new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        fireTableEvent(new TableClickEvent(GenericTaskTableDisplay.this, task, "name"));
                    }
                });
                assignmentLink.setCaptionAsHtml(true);
                assignmentLink.setStyleName(UIConstants.BUTTON_LINK);
                return assignmentLink;
            }
        });

        addGeneratedColumn("assignUser", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                ProjectGenericTask task = getBeanByIndex(itemId);
                return new ProjectMemberLink(task.getAssignUser(), task.getAssignUserAvatarId(), task.getAssignUserFullName());
            }

        });

        addGeneratedColumn("dueDate", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(final Table source,
                                                        final Object itemId, final Object columnId) {
                ProjectGenericTask task = getBeanByIndex(itemId);
                return new ELabel().prettyDate(task.getDueDate());
            }
        });
    }
}
