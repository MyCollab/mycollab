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
package com.mycollab.mobile.module.project.view.milestone;

import com.mycollab.core.MyCollabException;
import com.mycollab.mobile.module.project.view.AbstractProjectPresenter;
import com.mycollab.mobile.module.project.view.parameters.MilestoneScreenData;
import com.mycollab.mobile.mvp.view.PresenterOptionUtil;
import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class MilestonePresenter extends AbstractProjectPresenter<MilestoneContainer> {
    private static final long serialVersionUID = 5263058263047835714L;

    public MilestonePresenter() {
        super(MilestoneContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        IPresenter<?> presenter;

        if (data instanceof MilestoneScreenData.Search) {
            presenter = PresenterResolver.getPresenter(MilestoneListPresenter.class);
        } else if (data instanceof MilestoneScreenData.Add || data instanceof MilestoneScreenData.Edit) {
            presenter = PresenterOptionUtil.getPresenter(IMilestoneAddPresenter.class);
        } else if (data instanceof MilestoneScreenData.Read) {
            presenter = PresenterResolver.getPresenter(MilestoneReadPresenter.class);
        } else {
            throw new MyCollabException("Do not support screen data " + data);
        }

        presenter.go(container, data);
    }

}
