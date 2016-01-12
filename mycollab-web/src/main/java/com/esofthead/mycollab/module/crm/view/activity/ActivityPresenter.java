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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.esofthead.mycollab.module.crm.view.CrmModule;
import com.esofthead.mycollab.module.crm.view.parameters.ActivityScreenData;
import com.esofthead.mycollab.module.crm.view.parameters.AssignmentScreenData;
import com.esofthead.mycollab.module.crm.view.parameters.CallScreenData;
import com.esofthead.mycollab.module.crm.view.parameters.MeetingScreenData;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ActivityPresenter extends AbstractPresenter<ActivityContainer> {
    private static final long serialVersionUID = 1L;

    public ActivityPresenter() {
        super(ActivityContainer.class);
    }

    @Override
    public void go(ComponentContainer container, ScreenData<?> data) {
        super.go(container, data, false);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.ACTIVITY);
        ActivityRootView activityContainer = (ActivityRootView) container;
        ActivityContainer eventContainer = (ActivityContainer) activityContainer
                .gotoView(AppContext.getMessage(ActivityI18nEnum.TAB_ACTIVITY_TITLE));

        AbstractPresenter presenter;

        if (data instanceof AssignmentScreenData.Add) {
            presenter = PresenterResolver.getPresenter(AssignmentAddPresenter.class);
        } else if (data instanceof AssignmentScreenData.Edit) {
            presenter = PresenterResolver.getPresenter(AssignmentAddPresenter.class);
        } else if (data instanceof AssignmentScreenData.Read) {
            presenter = PresenterResolver.getPresenter(AssignmentReadPresenter.class);
        } else if (data instanceof MeetingScreenData.Add) {
            presenter = PresenterResolver.getPresenter(MeetingAddPresenter.class);
        } else if (data instanceof MeetingScreenData.Edit) {
            presenter = PresenterResolver.getPresenter(MeetingAddPresenter.class);
        } else if (data instanceof MeetingScreenData.Read) {
            presenter = PresenterResolver.getPresenter(MeetingReadPresenter.class);
        } else if (data instanceof CallScreenData.Add) {
            presenter = PresenterResolver.getPresenter(CallAddPresenter.class);
        } else if (data instanceof CallScreenData.Edit) {
            presenter = PresenterResolver.getPresenter(CallAddPresenter.class);
        } else if (data instanceof CallScreenData.Read) {
            presenter = PresenterResolver.getPresenter(CallReadPresenter.class);
        } else if (data instanceof ActivityScreenData.GotoActivityList) {
            presenter = PresenterResolver.getPresenter(ActivityListPresenter.class);
        } else {
            throw new MyCollabException("Do not support data " + data);
        }

        presenter.go(eventContainer, data);
    }

}
