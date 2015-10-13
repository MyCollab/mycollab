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

package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.view.ProjectView;
import com.esofthead.mycollab.module.project.view.parameters.MilestoneScreenData;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MilestonePresenter extends AbstractPresenter<MilestoneContainer> {
    private static final long serialVersionUID = 1L;

    public MilestonePresenter() {
        super(MilestoneContainer.class);
    }

    @Override
    public void go(ComponentContainer container, ScreenData<?> data) {
        super.go(container, data, false);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        ProjectView projectViewContainer = (ProjectView) container;
        projectViewContainer.gotoSubView(ProjectTypeConstants.MILESTONE);

        AbstractPresenter presenter;
        if (data instanceof MilestoneScreenData.Search) {
            presenter = PresenterResolver.getPresenter(MilestoneListPresenter.class);
        } else if (data instanceof MilestoneScreenData.Add || data instanceof MilestoneScreenData.Edit) {
            presenter = PresenterResolver.getPresenter(MilestoneAddPresenter.class);
        } else if (data instanceof MilestoneScreenData.Read) {
            presenter = PresenterResolver.getPresenter(MilestoneReadPresenter.class);
        } else if (data instanceof MilestoneScreenData.Roadmap) {
            presenter = PresenterResolver.getPresenter(MilestoneRoadmapPresenter.class);
        } else {
            throw new MyCollabException("Do not support screen data " + data);
        }

        presenter.go(view, data);
    }

    @Override
    public void handleChain(ComponentContainer container, PageActionChain pageActionChain) {
        ScreenData pageAction = pageActionChain.peek();
        onGo(container, pageAction);
    }
}
