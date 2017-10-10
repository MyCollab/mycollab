/**
 * mycollab-web-community - Parent pom providing dependency and plugin management for applications
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
package com.mycollab.community.module.crm.view.setting;

import com.mycollab.module.crm.view.setting.CrmSettingContainer;
import com.mycollab.module.crm.view.setting.ICrmCustomView;
import com.mycollab.module.crm.view.setting.ICrmCustomViewPresenter;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class CrmCustomViewPresenter extends AbstractPresenter<ICrmCustomView> implements ICrmCustomViewPresenter {
    private static final long serialVersionUID = 1L;

    public CrmCustomViewPresenter() {
        super(ICrmCustomView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmSettingContainer settingContainer = (CrmSettingContainer) container;
        settingContainer.gotoSubView("Custom Layouts");
        AppUI.addFragment("crm/setting/customlayout", "Custom Layouts");
    }
}
