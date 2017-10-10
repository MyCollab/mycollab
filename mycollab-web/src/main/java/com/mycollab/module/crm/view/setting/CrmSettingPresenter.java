/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.setting;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.ClassUtils;
import com.mycollab.module.crm.data.CustomViewScreenData;
import com.mycollab.module.crm.data.NotificationSettingScreenData;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmSettingPresenter extends CrmGenericPresenter<CrmSettingContainer> {
    private static final long serialVersionUID = 1L;

    public CrmSettingPresenter() {
        super(CrmSettingContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule crmModule = (CrmModule) container;
        crmModule.setContent(view);

        IPresenter presenter;
        if (ClassUtils.instanceOf(data, NotificationSettingScreenData.Read.class)) {
            presenter = PresenterResolver.getPresenter(CrmNotificationSettingPresenter.class);
        } else if (ClassUtils.instanceOf(data, CustomViewScreenData.Read.class)) {
            presenter = PresenterResolver.getPresenter(ICrmCustomViewPresenter.class);
        } else {
            throw new MyCollabException("Do not support screen data " + data);
        }

        presenter.go(view, data);
    }
}
