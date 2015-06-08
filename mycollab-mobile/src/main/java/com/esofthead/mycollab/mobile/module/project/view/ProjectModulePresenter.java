/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.MobileApplication;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.ui.AbstractMobilePresenter;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.extensions.LocalStorage;
import com.vaadin.addon.touchkit.extensions.LocalStorageCallback;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Inc.
 *
 * @since 4.3.1
 */
public class ProjectModulePresenter extends AbstractMobilePresenter<ProjectModule> {
    private static final long serialVersionUID = 6940806138148601147L;

    public ProjectModulePresenter() {
        super(ProjectModule.class);
    }

    @Override
    protected void onGo(ComponentContainer navigator, ScreenData<?> data) {
        checkLocalData();
    }

    private void checkLocalData() {
        LocalStorage.detectValue(MobileApplication.LOGIN_DATA,
                new LocalStorageCallback() {
                    private static final long serialVersionUID = 3217947479690600476L;

                    @Override
                    public void onSuccess(String value) {
                        if (value != null) {
                            String[] loginParams = value.split("\\$");
                            EventBusFactory.getInstance()
                                    .post(new ProjectEvent.PlainLogin(this, new String[]{loginParams[0],
                                            PasswordEncryptHelper.decryptText(loginParams[1]), String.valueOf(false)}));

                        } else {
                            EventBusFactory.getInstance().post(new ProjectEvent.GotoLogin(this, null));
                        }
                    }

                    @Override
                    public void onFailure(FailureEvent error) {
                        EventBusFactory.getInstance().post(new ProjectEvent.GotoLogin(this, null));
                    }
                });
    }

}
