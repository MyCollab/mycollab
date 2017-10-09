package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.module.crm.event.ActivityEvent;
import com.mycollab.mobile.module.crm.view.AbstractCrmPresenter;
import com.mycollab.mobile.shell.event.ShellEvent;
import com.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.mycollab.module.crm.service.MeetingService;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.DefaultEditFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class MeetingAddPresenter extends AbstractCrmPresenter<MeetingAddView> {
    private static final long serialVersionUID = -3427352135962459534L;

    public MeetingAddPresenter() {
        super(MeetingAddView.class);
    }

    @Override
    protected void postInitView() {
        getView().getEditFormHandlers().addFormHandler(new DefaultEditFormHandler<MeetingWithBLOBs>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final MeetingWithBLOBs item) {
                save(item);
                EventBusFactory.getInstance().post(new ShellEvent.NavigateBack(this, null));
            }

            @Override
            public void onSaveAndNew(final MeetingWithBLOBs item) {
                save(item);
                EventBusFactory.getInstance().post(new ActivityEvent.MeetingAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (UserUIContext.canWrite(RolePermissionCollections.CRM_MEETING)) {

            MeetingWithBLOBs meeting = null;
            if (data.getParams() instanceof MeetingWithBLOBs) {
                meeting = (MeetingWithBLOBs) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                MeetingService meetingService = AppContextUtil.getSpringBean(MeetingService.class);
                meeting = meetingService.findByPrimaryKey((Integer) data.getParams(), AppUI.getAccountId());
            }
            if (meeting == null) {
                NotificationUtil.showRecordNotExistNotification();
                return;
            }
            super.onGo(container, data);

            getView().editItem(meeting);

            if (meeting.getId() == null) {
                AppUI.addFragment("crm/activity/meeting/add/", UserUIContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
                        UserUIContext.getMessage(MeetingI18nEnum.SINGLE)));
            } else {
                AppUI.addFragment("crm/activity/meeting/edit/" + UrlEncodeDecoder.encode(meeting.getId()),
                        UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                                UserUIContext.getMessage(MeetingI18nEnum.SINGLE), meeting.getSubject()));
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

    public void save(MeetingWithBLOBs item) {
        MeetingService meetingService = AppContextUtil.getSpringBean(MeetingService.class);
        item.setSaccountid(AppUI.getAccountId());
        if (item.getId() == null) {
            meetingService.saveWithSession(item, UserUIContext.getUsername());
        } else {
            meetingService.updateWithSession(item, UserUIContext.getUsername());
        }
    }
}
