package com.mycollab.mobile.module.project.view.bug;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.module.project.event.BugEvent;
import com.mycollab.mobile.module.project.event.TicketEvent;
import com.mycollab.mobile.module.project.view.AbstractProjectPresenter;
import com.mycollab.mobile.ui.ConfirmDialog;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.service.BugService;
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
 * @since 4.5.2
 */
public class BugReadPresenter extends AbstractProjectPresenter<BugReadView> {
    private static final long serialVersionUID = -1031817390942006096L;

    public BugReadPresenter() {
        super(BugReadView.class);
    }

    @Override
    protected void postInitView() {
        getView().getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<SimpleBug>() {

            @Override
            public void onAdd(SimpleBug data) {
                EventBusFactory.getInstance().post(new BugEvent.GotoAdd(this, null));
            }

            @Override
            public void onDelete(final SimpleBug data) {
                ConfirmDialog.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        dialog -> {
                            if (dialog.isConfirmed()) {
                                BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                                bugService.removeWithSession(data, UserUIContext.getUsername(), AppUI.getAccountId());
                                EventBusFactory.getInstance().post(new TicketEvent.GotoDashboard(this, null));
                            }
                        });
            }

            @Override
            public void onClone(SimpleBug data) {
                SimpleBug cloneData = (SimpleBug) data.copy();
                cloneData.setId(null);
                EventBusFactory.getInstance().post(new BugEvent.GotoEdit(this, cloneData));
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.BUGS)) {
            if (data.getParams() instanceof Integer) {
                BugService bugService = AppContextUtil.getSpringBean(BugService.class);
                SimpleBug bug = bugService.findById((Integer) data.getParams(), AppUI.getAccountId());
                if (bug != null) {
                    getView().previewItem(bug);
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
