package com.mycollab.module.project.view.kanban;

import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.view.IKanbanView;
import com.mycollab.vaadin.UserUIContext;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @author MyCollab Ltd
 * @since 5.2.1
 */
public class DeleteColumnWindow extends MWindow {
    public DeleteColumnWindow(final IKanbanView kanbanView, final String type) {
        super(UserUIContext.getMessage(TaskI18nEnum.ACTION_DELETE_COLUMNS));
        this.withWidth("800px").withModal(true).withResizable(false).withCenter();
    }
}
