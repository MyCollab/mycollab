/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.esofthead.mycollab.module.user.accountsettings.team.view;

import com.esofthead.mycollab.module.user.accountsettings.view.parameters.RoleScreenData;
import com.esofthead.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class RolePresenter extends AbstractPresenter<RoleContainer> {
    private static final long serialVersionUID = 1L;

    public RolePresenter() {
        super(RoleContainer.class);
    }

    @Override
    public void go(ComponentContainer container, ScreenData<?> data) {
        super.go(container, data, false);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        UserPermissionManagementView groupContainer = (UserPermissionManagementView) container;
        groupContainer.gotoSubView("Roles");

        if (data == null) {
            RoleListPresenter listPresenter = PresenterResolver.getPresenter(RoleListPresenter.class);
            RoleSearchCriteria criteria = new RoleSearchCriteria();
            listPresenter.go(view.getWidget(), new ScreenData.Search<>(criteria));
        } else if (data instanceof RoleScreenData.Add
                || data instanceof RoleScreenData.Edit) {
            RoleAddPresenter presenter = PresenterResolver
                    .getPresenter(RoleAddPresenter.class);
            presenter.go(view.getWidget(), data);
        } else if (data instanceof RoleScreenData.Read) {
            RoleReadPresenter presenter = PresenterResolver.getPresenter(RoleReadPresenter.class);
            presenter.go(view.getWidget(), data);
        } else if (data instanceof RoleScreenData.Search) {
            RoleListPresenter presenter = PresenterResolver.getPresenter(RoleListPresenter.class);
            presenter.go(view.getWidget(), data);
        }
    }
}
