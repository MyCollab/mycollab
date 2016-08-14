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
package com.mycollab.module.project.view.task.components;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Span;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.web.ui.AbstractToggleSummaryField;
import com.mycollab.vaadin.web.ui.WebUIConstants;
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
public class ToggleTaskSummaryField extends AbstractToggleSummaryField {
    private boolean isRead = true;
    private SimpleTask task;
    private int maxLength;

    public ToggleTaskSummaryField(final SimpleTask task) {
        this(task, Integer.MAX_VALUE);
    }

    public ToggleTaskSummaryField(final SimpleTask task, int maxLength) {
        this.setWidth("100%");
        this.maxLength = maxLength;
        this.task = task;
        titleLinkLbl = ELabel.html(buildTaskLink()).withWidthUndefined().withStyleName(UIConstants.LABEL_WORD_WRAP);

        this.addComponent(titleLinkLbl);
        buttonControls = new MHorizontalLayout().withStyleName("toggle").withSpacing(false);
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
            this.addStyleName("editable-field");

            MButton instantEditBtn = new MButton("", clickEvent -> {
                if (isRead) {
                    ToggleTaskSummaryField.this.removeComponent(titleLinkLbl);
                    ToggleTaskSummaryField.this.removeComponent(buttonControls);
                    final TextField editField = new TextField();
                    editField.setValue(task.getTaskname());
                    editField.setWidth("100%");
                    editField.focus();
                    ToggleTaskSummaryField.this.addComponent(editField);
                    ToggleTaskSummaryField.this.removeStyleName("editable-field");
                    editField.addValueChangeListener(valueChangeEvent -> updateFieldValue(editField));
                    editField.addBlurListener(blurEvent -> updateFieldValue(editField));
                    isRead = !isRead;
                }
            }).withIcon(FontAwesome.EDIT).withStyleName(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_ICON_ALIGN_TOP);
            instantEditBtn.setDescription(AppContext.getMessage(TaskI18nEnum.OPT_EDIT_TASK_NAME));
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
        if (StringUtils.isNotBlank(newValue) && !newValue.equals(task.getTaskname())) {
            task.setTaskname(newValue);
            titleLinkLbl.setValue(buildTaskLink());
            ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
            taskService.updateSelectiveWithSession(BeanUtility.deepClone(task), AppContext.getUsername());
        }

        isRead = !isRead;
    }

    private String buildTaskLink() {
        String linkName = StringUtils.trim(task.getTaskname(), maxLength, true);
        A taskLink = new A().setId("tag" + TOOLTIP_ID).setHref(ProjectLinkBuilder.generateTaskPreviewFullLink(task.getTaskkey(),
                CurrentProjectVariables.getShortName())).appendText(linkName).setStyle("display:inline");
        Div resultDiv = new DivLessFormatter().appendChild(taskLink);
        if (task.isOverdue()) {
            taskLink.setCSSClass("overdue");
            resultDiv.appendChild(new Span().setCSSClass(UIConstants.META_INFO).appendText(" - " + AppContext
                    .getMessage(ProjectCommonI18nEnum.OPT_DUE_IN, AppContext.formatDuration(task.getDeadline()))));
        } else if (task.isCompleted()) {
            taskLink.setCSSClass("completed");
        }

        taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ProjectTypeConstants.TASK, task.getId() + ""));
        taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

        return resultDiv.write();
    }

    public void updateLabel() {
        titleLinkLbl.setValue(buildTaskLink());
    }

    public void closeTask() {
        titleLinkLbl.removeStyleName("overdue");
        titleLinkLbl.addStyleName("completed");
    }

    public void overdueTask() {
        titleLinkLbl.removeStyleName("completed");
        titleLinkLbl.addStyleName("overdue");
    }

    public void reOpenTask() {
        titleLinkLbl.removeStyleName("overdue completed");
    }
}
