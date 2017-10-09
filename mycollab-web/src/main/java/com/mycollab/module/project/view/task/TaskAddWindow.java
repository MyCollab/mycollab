package com.mycollab.module.project.view.task;

import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.vaadin.UserUIContext;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class TaskAddWindow extends MWindow {
    public TaskAddWindow(SimpleTask task) {
        setCaption(task.getId() == null ? UserUIContext.getMessage(TaskI18nEnum.NEW) : UserUIContext.getMessage(TaskI18nEnum.DETAIL));

        TaskEditForm editForm = new TaskEditForm() {
            @Override
            protected void postExecution() {
                close();
            }
        };
        editForm.setBean(task);
        this.withWidth("1200px").withModal(true).withResizable(false).withContent(editForm).withCenter();
    }
}
