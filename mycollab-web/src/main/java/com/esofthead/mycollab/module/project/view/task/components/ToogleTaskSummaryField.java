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
package com.esofthead.mycollab.module.project.view.task.components;

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import java.util.UUID;

/**
 * @author MyCollab Ltd
 * @since 5.2.3
 */
public class ToogleTaskSummaryField extends CssLayout {
    private boolean isRead = true;
    private Label taskLinkLbl;
    private SimpleTask task;
    private int maxLength;

    public ToogleTaskSummaryField(final SimpleTask task) {
        this(task, Integer.MAX_VALUE);
    }

    public ToogleTaskSummaryField(final SimpleTask task, int maxLength) {
        this.setWidth("100%");
        this.maxLength = maxLength;
        this.task = task;
        taskLinkLbl = new Label(buildTaskLink(), ContentMode.HTML);
        taskLinkLbl.setWidthUndefined();
        taskLinkLbl.addStyleName(UIConstants.LABEL_WORD_WRAP);

        this.addComponent(taskLinkLbl);
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
            this.addStyleName("editable-field");
            this.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                    if (event.getClickedComponent() == taskLinkLbl) {
                        return;
                    }
                    if (isRead) {
                        ToogleTaskSummaryField.this.removeComponent(taskLinkLbl);
                        final TextField editField = new TextField();
                        editField.setValue(task.getTaskname());
                        editField.setWidth("100%");
                        editField.focus();
                        ToogleTaskSummaryField.this.addComponent(editField);
                        ToogleTaskSummaryField.this.removeStyleName("editable-field");
                        editField.addValueChangeListener(new Property.ValueChangeListener() {
                            @Override
                            public void valueChange(Property.ValueChangeEvent event) {
                                updateFieldValue(editField);
                            }
                        });
                        editField.addBlurListener(new FieldEvents.BlurListener() {
                            @Override
                            public void blur(FieldEvents.BlurEvent event) {
                                updateFieldValue(editField);
                            }
                        });
                        isRead = !isRead;
                    }

                }
            });
        }
    }

    private void updateFieldValue(TextField editField) {
        removeComponent(editField);
        addComponent(taskLinkLbl);
        addStyleName("editable-field");
        String newValue = editField.getValue();
        if (StringUtils.isNotBlank(newValue) && !newValue.equals(task.getTaskname())) {
            task.setTaskname(newValue);
            taskLinkLbl.setValue(buildTaskLink());
            ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
            taskService.updateWithSession(task, AppContext.getUsername());
        }

        isRead = !isRead;
    }

    private String buildTaskLink() {
        String uid = UUID.randomUUID().toString();
        String linkName = String.format("[#%d] - %s", task.getTaskkey(), StringUtils.trim(task.getTaskname(), maxLength, true));
        A taskLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateTaskPreviewFullLink(task.getTaskkey(),
                CurrentProjectVariables.getShortName())).appendText(linkName).setStyle("display:inline");
        Div resultDiv = new DivLessFormatter().appendChild(taskLink);
        if (task.isOverdue()) {
            taskLink.setCSSClass("overdue");
            resultDiv.appendChild(new Span().setCSSClass(UIConstants.LABEL_META_INFO).appendText(" - Due in " + AppContext
                    .formatDuration(task.getDeadline())));
        } else if (task.isCompleted()) {
            taskLink.setCSSClass("completed");
        }

        taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, ProjectTypeConstants.TASK, task.getId() + ""));
        taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));

        resultDiv.appendChild(TooltipHelper.buildDivTooltipEnable(uid));
        return resultDiv.write();
    }

    public void updateLabel() {
        taskLinkLbl.setValue(buildTaskLink());
    }

    public void closeTask() {
        taskLinkLbl.removeStyleName("overdue pending");
        taskLinkLbl.addStyleName("completed");
    }

    public void overdueTask() {
        taskLinkLbl.removeStyleName("completed pending");
        taskLinkLbl.addStyleName("overdue");
    }

    public void reOpenTask() {
        taskLinkLbl.removeStyleName("overdue pending completed");
    }

    public void pendingTask() {
        taskLinkLbl.removeStyleName("overdue completed");
        taskLinkLbl.addStyleName("pending");
    }
}
