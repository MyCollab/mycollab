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
package com.mycollab.module.project.view.milestone;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.core.IgnoreException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.ProjectGenericTask;
import com.mycollab.module.project.domain.Risk;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.service.RiskService;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.AbstractToggleSummaryField;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import static com.mycollab.vaadin.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd
 * @since 5.2.3
 */
public class ToggleGenericTaskSummaryField extends AbstractToggleSummaryField {
    private ProjectGenericTask genericTask;
    private boolean isRead = true;

    ToggleGenericTaskSummaryField(final ProjectGenericTask genericTask) {
        this.genericTask = genericTask;
        this.setWidth("100%");
        titleLinkLbl = ELabel.html(buildGenericTaskLink()).withStyleName(ValoTheme.LABEL_NO_MARGIN,
                UIConstants.LABEL_WORD_WRAP).withWidthUndefined();
        this.addComponent(titleLinkLbl);
        if ((genericTask.isTask() && CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) ||
                (genericTask.isBug() && CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.BUGS)) ||
                (genericTask.isRisk() && CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.RISKS))) {
            this.addStyleName("editable-field");
            buttonControls = new MHorizontalLayout().withStyleName("toggle").withSpacing(false);
            MButton instantEditBtn = new MButton("", clickEvent -> {
                if (isRead) {
                    removeComponent(titleLinkLbl);
                    removeComponent(buttonControls);
                    final TextField editField = new TextField();
                    editField.setValue(genericTask.getName());
                    editField.setWidth("100%");
                    editField.focus();
                    addComponent(editField);
                    removeStyleName("editable-field");
                    editField.addValueChangeListener(valueChangeEvent -> updateFieldValue(editField));
                    editField.addBlurListener(blurEvent -> updateFieldValue(editField));
                    isRead = !isRead;
                }
            }).withIcon(FontAwesome.EDIT).withStyleName(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_ICON_ALIGN_TOP);
            instantEditBtn.setDescription("Edit task name");
            buttonControls.with(instantEditBtn);

            this.addComponent(buttonControls);
        }
    }

    private void updateFieldValue(TextField editField) {
        removeComponent(editField);
        addComponent(titleLinkLbl);
        addComponent(buttonControls);
        addStyleName("editable-field");
        String newValue = editField.getValue();
        if (StringUtils.isNotBlank(newValue) && !newValue.equals(genericTask.getName())) {
            genericTask.setName(newValue);
            titleLinkLbl.setValue(buildGenericTaskLink());
            if (genericTask.isBug()) {
                BugWithBLOBs bug = new BugWithBLOBs();
                bug.setId(genericTask.getTypeId());
                bug.setSummary(genericTask.getName());
                bug.setSaccountid(AppContext.getAccountId());
                BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                bugService.updateSelectiveWithSession(bug, AppContext.getUsername());
            } else if (genericTask.isTask()) {
                Task task = new Task();
                task.setId(genericTask.getTypeId());
                task.setTaskname(genericTask.getName());
                task.setSaccountid(AppContext.getAccountId());
                ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                taskService.updateSelectiveWithSession(task, AppContext.getUsername());
            } else if (genericTask.isRisk()) {
                Risk risk = new Risk();
                risk.setId(genericTask.getTypeId());
                risk.setRiskname(genericTask.getName());
                risk.setSaccountid(AppContext.getAccountId());
                RiskService riskService = AppContextUtil.getSpringBean(RiskService.class);
                riskService.updateSelectiveWithSession(risk, AppContext.getUsername());
            }
        }

        isRead = !isRead;
    }

    private String buildGenericTaskLink() {
        Div issueDiv = new Div();

        A taskLink = new A().setId("tag" + TOOLTIP_ID);
        if (genericTask.isBug() || genericTask.isTask()) {
            taskLink.setHref(ProjectLinkBuilder.generateProjectItemLink(genericTask.getProjectShortName(),
                    genericTask.getProjectId(), genericTask.getType(), genericTask.getExtraTypeId() + ""));
        } else if (genericTask.isRisk()) {
            taskLink.setHref(ProjectLinkBuilder.generateProjectItemLink(genericTask.getProjectShortName(),
                    genericTask.getProjectId(), genericTask.getType(), genericTask.getTypeId() + ""));
        } else {
            throw new IgnoreException("Not support type: " + genericTask.getType());
        }

        taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(genericTask.getType(), genericTask.getTypeId() + ""));
        taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
        taskLink.appendText(genericTask.getName());

        issueDiv.appendChild(taskLink);

        if (genericTask.isClosed()) {
            taskLink.setCSSClass("completed");
        } else if (genericTask.isOverdue()) {
            taskLink.setCSSClass("overdue");
            issueDiv.appendChild(new Span().setCSSClass(UIConstants.META_INFO).appendText(" - Due in " + AppContext
                    .formatDuration(genericTask.getDueDate())));
        }
        return issueDiv.write();
    }
}
