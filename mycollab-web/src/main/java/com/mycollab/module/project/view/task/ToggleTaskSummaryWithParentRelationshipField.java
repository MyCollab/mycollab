package com.mycollab.module.project.view.task;

import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.RemoveInlineComponentMarker;
import com.mycollab.vaadin.ui.UIUtils;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
public class ToggleTaskSummaryWithParentRelationshipField extends CustomField<SimpleTask> {
    private ToggleTaskSummaryField toggleTaskSummaryField;

    public ToggleTaskSummaryWithParentRelationshipField(final SimpleTask task) {
        toggleTaskSummaryField = new ToggleTaskSummaryField(task, false);
        MButton unlinkBtn = new MButton("", clickEvent -> {
            task.setParenttaskid(null);
            ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
            taskService.updateWithSession(task, UserUIContext.getUsername());
            UIUtils.removeChildAssociate(ToggleTaskSummaryWithParentRelationshipField.this, RemoveInlineComponentMarker.class);
        }).withIcon(FontAwesome.UNLINK).withStyleName(ValoTheme.BUTTON_ICON_ALIGN_TOP, ValoTheme.BUTTON_ICON_ONLY)
                .withDescription(UserUIContext.getMessage(TaskI18nEnum.OPT_REMOVE_PARENT_CHILD_RELATIONSHIP));
        toggleTaskSummaryField.addControl(unlinkBtn);
    }

    @Override
    protected Component initContent() {
        return toggleTaskSummaryField;
    }

    @Override
    public Class<? extends SimpleTask> getType() {
        return SimpleTask.class;
    }

    public void updateLabel() {
        toggleTaskSummaryField.updateLabel();
    }

    public void closeTask() {
        toggleTaskSummaryField.closeTask();
    }

    public void overdueTask() {
        toggleTaskSummaryField.overdueTask();
    }

    public void reOpenTask() {
        toggleTaskSummaryField.reOpenTask();
    }
}
