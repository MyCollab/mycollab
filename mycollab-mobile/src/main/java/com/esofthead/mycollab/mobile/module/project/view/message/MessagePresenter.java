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
package com.esofthead.mycollab.mobile.module.project.view.message;

import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.InsideProjectNavigationMenu;
import com.esofthead.mycollab.mobile.module.project.view.parameters.MessageScreenData;
import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class MessagePresenter extends AbstractPresenter<MessageContainer> {
    private static final long serialVersionUID = 2423914044838645060L;

    public MessagePresenter() {
        super(MessageContainer.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.MESSAGES)) {

            InsideProjectNavigationMenu projectModuleMenu = (InsideProjectNavigationMenu) ((MobileNavigationManager) UI
                    .getCurrent().getContent()).getNavigationMenu();
            projectModuleMenu.selectButton(AppContext
                    .getMessage(ProjectCommonI18nEnum.VIEW_MESSAGE));

            if (data instanceof MessageScreenData.Read) {
                MessageReadPresenter presenter = PresenterResolver
                        .getPresenter(MessageReadPresenter.class);
                presenter.go(container, data);
            } else if (data instanceof MessageScreenData.Search) {
                MessageListPresenter presenter = PresenterResolver
                        .getPresenter(MessageListPresenter.class);
                presenter.go(container, data);
            } else if (data instanceof MessageScreenData.Add) {
                MessageAddPresenter presenter = PresenterResolver
                        .getPresenter(MessageAddPresenter.class);
                presenter.go(container, data);
            } else if (data == null) {
                MessageSearchCriteria searchCriteria = new MessageSearchCriteria();
                searchCriteria.setProjectids(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
                MessageListPresenter presenter = PresenterResolver
                        .getPresenter(MessageListPresenter.class);
                presenter.go(container,
                        new ScreenData.Preview<>(
                                searchCriteria));
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

}
