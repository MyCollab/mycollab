package com.mycollab.mobile.module.project.view.task;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.module.project.event.TaskEvent;
import com.mycollab.mobile.module.project.view.AbstractProjectPresenter;
import com.mycollab.mobile.shell.event.ShellEvent;
import com.mycollab.mobile.ui.ConfirmDialog;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class TaskReadPresenter extends AbstractProjectPresenter<TaskReadView> {
    private static final long serialVersionUID = 1L;

    public TaskReadPresenter() {
        super(TaskReadView.class);
    }

    @Override
    protected void postInitView() {
        this.getView().getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleTask>() {

            @Override
            public void onDelete(final SimpleTask data) {
                ConfirmDialog.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        dialog -> {
                            if (dialog.isConfirmed()) {
                                ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                                taskService.removeWithSession(data, UserUIContext.getUsername(), AppUI.getAccountId());
                                EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
                            }
                        });
            }

            @Override
            public void onClone(final SimpleTask data) {
                final Task cloneData = (Task) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new TaskEvent.GotoEdit(this, cloneData));
            }
        });
    }

    @Override
    protected void onGo(final HasComponents container, final ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.TASKS)) {
            if (data.getParams() instanceof Integer) {
                ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
                SimpleTask task = taskService.findById((Integer) data.getParams(), AppUI.getAccountId());

                if (task != null) {
                    getView().previewItem(task);
                    super.onGo(container, data);
                } else {
                    NotificationUtil.showRecordNotExistNotification();
                }
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
