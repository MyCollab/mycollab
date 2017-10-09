package com.mycollab.module.crm.view.setting;

import com.mycollab.module.crm.domain.CrmNotificationSetting;
import com.mycollab.module.crm.service.CrmNotificationSettingService;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmNotificationSettingPresenter extends CrmGenericPresenter<CrmNotificationSettingView> {
    private static final long serialVersionUID = 1L;

    public CrmNotificationSettingPresenter() {
        super(CrmNotificationSettingView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmSettingContainer settingContainer = (CrmSettingContainer) container;
        settingContainer.gotoSubView("notification");

        CrmNotificationSettingService service = AppContextUtil.getSpringBean(CrmNotificationSettingService.class);
        CrmNotificationSetting setting = service.findNotification(UserUIContext.getUsername(), AppUI.getAccountId());
        view.showNotificationSettings(setting);

        AppUI.addFragment("crm/setting/notification", "Notification Settings");
    }
}
