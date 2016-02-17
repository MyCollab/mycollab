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
package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.core.IgnoreException;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.ProjectGenericTask;
import com.esofthead.mycollab.module.project.domain.Risk;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.service.RiskService;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import java.util.UUID;

/**
 * @author MyCollab Ltd
 * @since 5.2.3
 */
public class ToogleGenericTaskSummaryField extends CssLayout {
    private ProjectGenericTask genericTask;
    private ELabel taskLbl;
    private boolean isRead = true;

    ToogleGenericTaskSummaryField(final ProjectGenericTask genericTask) {
        this.genericTask = genericTask;
        this.setWidth("100%");
        taskLbl = new ELabel(buildGenericTaskLink(), ContentMode.HTML).withWidthUndefined();
        taskLbl.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        taskLbl.addStyleName(UIConstants.LABEL_WORD_WRAP);
        this.addComponent(taskLbl);
        if ((genericTask.isTask() && CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) ||
                (genericTask.isBug() && CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS))) {
            this.addStyleName("editable-field");
            this.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                    if (event.getClickedComponent() == taskLbl) {
                        return;
                    }
                    if (isRead) {
                        ToogleGenericTaskSummaryField.this.removeComponent(taskLbl);
                        final TextField editField = new TextField();
                        editField.setValue(genericTask.getName());
                        editField.setWidth("100%");
                        editField.focus();
                        ToogleGenericTaskSummaryField.this.addComponent(editField);
                        ToogleGenericTaskSummaryField.this.removeStyleName("editable-field");
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
        addComponent(taskLbl);
        addStyleName("editable-field");
        String newValue = editField.getValue();
        if (StringUtils.isNotBlank(newValue) && !newValue.equals(genericTask.getName())) {
            genericTask.setName(newValue);
            taskLbl.setValue(buildGenericTaskLink());
            if (genericTask.isBug()) {
                BugWithBLOBs bug = new BugWithBLOBs();
                bug.setId(genericTask.getTypeId());
                bug.setSummary(genericTask.getName());
                bug.setSaccountid(AppContext.getAccountId());
                BugService bugService = ApplicationContextUtil.getSpringBean(BugService.class);
                bugService.updateSelectiveWithSession(bug, AppContext.getUsername());
            } else if (genericTask.isTask()) {
                Task task = new Task();
                task.setId(genericTask.getTypeId());
                task.setTaskname(genericTask.getName());
                task.setSaccountid(AppContext.getAccountId());
                ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                taskService.updateSelectiveWithSession(task, AppContext.getUsername());
            } else if (genericTask.isRisk()) {
                Risk risk = new Risk();
                risk.setId(genericTask.getTypeId());
                risk.setRiskname(genericTask.getName());
                risk.setSaccountid(AppContext.getAccountId());
                RiskService riskService = ApplicationContextUtil.getSpringBean(RiskService.class);
                riskService.updateSelectiveWithSession(risk, AppContext.getUsername());
            }
        }

        isRead = !isRead;
    }

    private String buildGenericTaskLink() {
        Div issueDiv = new Div();
        String uid = UUID.randomUUID().toString();
        A taskLink = new A().setId("tag" + uid);
        if (genericTask.isBug() || genericTask.isTask()) {
            taskLink.setHref(ProjectLinkBuilder.generateProjectItemLink(genericTask.getProjectShortName(),
                    genericTask.getProjectId(), genericTask.getType(), genericTask.getExtraTypeId() + ""));
        } else if (genericTask.isRisk()) {
            taskLink.setHref(ProjectLinkBuilder.generateProjectItemLink(genericTask.getProjectShortName(),
                    genericTask.getProjectId(), genericTask.getType(), genericTask.getTypeId() + ""));
        } else {
            throw new IgnoreException("Not support type: " + genericTask.getType());
        }

        taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(uid, genericTask.getType(), genericTask.getTypeId() + ""));
        taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
        if (genericTask.isBug() || genericTask.isTask()) {
            taskLink.appendText(String.format("[#%d] - %s", genericTask.getExtraTypeId(), genericTask.getName()));
        } else if (genericTask.isRisk()) {
            taskLink.appendText(genericTask.getName());
        }

        issueDiv.appendChild(taskLink);

        if (genericTask.isClosed()) {
            taskLink.setCSSClass("completed");
        } else if (genericTask.isOverdue()) {
            taskLink.setCSSClass("overdue");
            issueDiv.appendChild(new Span().setCSSClass(UIConstants.LABEL_META_INFO).appendText(" - Due in " + AppContext
                    .formatDuration(genericTask.getDueDate())));
        }
        issueDiv.appendChild(TooltipHelper.buildDivTooltipEnable(uid));
        return issueDiv.write();
    }
}
