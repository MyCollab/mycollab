package com.mycollab.module.crm.view.setting;

import com.mycollab.module.crm.domain.CrmNotificationSetting;
import com.mycollab.vaadin.mvp.PageView;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface CrmNotificationSettingView extends PageView {

    void showNotificationSettings(CrmNotificationSetting notification);
}
