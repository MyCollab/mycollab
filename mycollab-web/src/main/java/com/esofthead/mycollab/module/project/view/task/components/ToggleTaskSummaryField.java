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
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import static com.esofthead.mycollab.utils.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd
 * @since 5.2.3
 */
public class ToggleTaskSummaryField extends CssLayout {
    private boolean isRead = true;
    private SimpleTask task;
    private int maxLength;
    private Label taskLinkLbl;
    private MHorizontalLayout buttonControls;

    public ToggleTaskSummaryField(final SimpleTask task) {
        this(task, Integer.MAX_VALUE, false);
    }

    public ToggleTaskSummaryField(final SimpleTask task, int maxLength, boolean canRemoveParentLink) {
        this.setWidth("100%");
        this.maxLength = maxLength;
        this.task = task;
        taskLinkLbl = new Label(buildTaskLink(), ContentMode.HTML);
        taskLinkLbl.setWidthUndefined();
        taskLinkLbl.addStyleName(UIConstants.LABEL_WORD_WRAP);

        this.addComponent(taskLinkLbl);
        buttonControls = new MHorizontalLayout().withStyleName("toggle").withSpacing(false);
        if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
            this.addStyleName("editable-field");

            Button instantEditBtn = new Button(null, new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    if (isRead) {
                        ToggleTaskSummaryField.this.removeComponent(taskLinkLbl);
                        ToggleTaskSummaryField.this.removeComponent(buttonControls);
                        final TextField editField = new TextField();
                        editField.setValue(task.getTaskname());
                        editField.setWidth("100%");
                        editField.focus();
                        ToggleTaskSummaryField.this.addComponent(editField);
                        ToggleTaskSummaryField.this.removeStyleName("editable-field");
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
            instantEditBtn.setDescription("Edit task name");
            instantEditBtn.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
            instantEditBtn.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
            instantEditBtn.setIcon(FontAwesome.EDIT);
            buttonControls.with(instantEditBtn);

            if (canRemoveParentLink) {
                Button unlinkBtn = new Button(null, new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        task.setParenttaskid(null);
                        ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                        taskService.updateWithSession(task, AppContext.getUsername());
                        fireEvent(null);
                    }
                });
                unlinkBtn.setIcon(FontAwesome.UNLINK);
                unlinkBtn.setDescription("Remove parent-child relationship");
                unlinkBtn.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP);
                unlinkBtn.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
                buttonControls.with(unlinkBtn);
            }

            this.addComponent(buttonControls);
        }
    }

    private void updateFieldValue(TextField editField) {
        removeComponent(editField);
        addComponent(taskLinkLbl);
        addComponent(buttonControls);
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
        String linkName = String.format("[#%d] - %s", task.getTaskkey(), StringUtils.trim(task.getTaskname(), maxLength, true));
        A taskLink = new A().setId("tag" + TOOLTIP_ID).setHref(ProjectLinkBuilder.generateTaskPreviewFullLink(task.getTaskkey(),
                CurrentProjectVariables.getShortName())).appendText(linkName).setStyle("display:inline");
        Div resultDiv = new DivLessFormatter().appendChild(taskLink);
        if (task.isOverdue()) {
            taskLink.setCSSClass("overdue");
            resultDiv.appendChild(new Span().setCSSClass(UIConstants.LABEL_META_INFO).appendText(" - Due in " + AppContext
                    .formatDuration(task.getDeadline())));
        } else if (task.isCompleted()) {
            taskLink.setCSSClass("completed");
        }

        taskLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ProjectTypeConstants.TASK, task.getId() + ""));
        taskLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());

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
