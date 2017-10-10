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
package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.billing.RegisterStatusConstants;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.accountsettings.view.parameters.UserScreenData;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class UserPresenter extends AbstractPresenter<UserContainer> {
    private static final long serialVersionUID = 1L;

    public UserPresenter() {
        super(UserContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        UserPermissionManagementView groupContainer = (UserPermissionManagementView) container;
        groupContainer.gotoSubView(UserUIContext.getMessage(UserI18nEnum.LIST));

        if (data == null) {
            UserListPresenter listPresenter = PresenterResolver.getPresenter(UserListPresenter.class);
            UserSearchCriteria criteria = new UserSearchCriteria();
            criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
            criteria.setRegisterStatuses(new SetSearchField(RegisterStatusConstants.ACTIVE, RegisterStatusConstants.NOT_LOG_IN_YET));
            listPresenter.go(view, new ScreenData.Search<>(criteria));
        } else if (data instanceof UserScreenData.Read) {
            UserReadPresenter presenter = PresenterResolver.getPresenter(UserReadPresenter.class);
            presenter.go(view, data);
        } else if (data instanceof UserScreenData.Search) {
            UserListPresenter presenter = PresenterResolver.getPresenter(UserListPresenter.class);
            presenter.go(view, data);
        } else if (data instanceof UserScreenData.Add || data instanceof UserScreenData.Edit) {
            UserAddPresenter presenter = PresenterResolver.getPresenter(UserAddPresenter.class);
            presenter.go(view, data);
        }
    }
}
