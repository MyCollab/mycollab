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

package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.ClassUtils;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.view.ProjectView;
import com.esofthead.mycollab.module.project.view.parameters.TaskGroupScreenData;
import com.esofthead.mycollab.module.project.view.parameters.TaskScreenData;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskPresenter extends AbstractPresenter<TaskContainer> {
    private static final long serialVersionUID = 1L;

    public TaskPresenter() {
        super(TaskContainer.class);
    }

    @Override
    public void go(ComponentContainer container, ScreenData<?> data) {
        super.go(container, data, false);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        ProjectView projectViewContainer = (ProjectView) container;
        projectViewContainer.gotoSubView(ProjectTypeConstants.TASK);

        view.removeAllComponents();

        AbstractPresenter<?> presenter;

        if (data instanceof TaskScreenData.Read) {
            presenter = PresenterResolver.getPresenter(TaskReadPresenter.class);
        } else if (data instanceof TaskGroupScreenData.Read) {
            presenter = PresenterResolver
                    .getPresenter(TaskGroupReadPresenter.class);
        } else if (ClassUtils.instanceOf(data, TaskScreenData.Edit.class,
                TaskScreenData.Add.class)) {
            presenter = PresenterResolver.getPresenter(TaskAddPresenter.class);
        } else if (ClassUtils.instanceOf(data, TaskGroupScreenData.Add.class,
                TaskGroupScreenData.Edit.class)) {
            presenter = PresenterResolver
                    .getPresenter(TaskGroupAddPresenter.class);
        } else if (data instanceof TaskGroupScreenData.ReorderTaskListRequest) {
            presenter = PresenterResolver
                    .getPresenter(TaskGroupReorderPresenter.class);
        } else if (data instanceof TaskGroupScreenData.GotoDashboard
                || data == null) {
            presenter = PresenterResolver
                    .getPresenter(TaskGroupDisplayPresenter.class);
        } else if (data instanceof TaskGroupScreenData.GotoGanttChartView) {
            presenter = PresenterResolver
                    .getPresenter(GanttChartViewPresenter.class);
        } else if (data instanceof TaskScreenData.Search) {
            presenter = PresenterResolver
                    .getPresenter(TaskSearchPresenter.class);
        } else if (data instanceof TaskScreenData.GanttChart) {
            presenter = PresenterResolver.getPresenter(GanttChartViewPresenter.class);
        } else {
            throw new MyCollabException("No support data: " + data);
        }

        presenter.go(view, data);
    }

    @Override
    public void handleChain(ComponentContainer container,
                            PageActionChain pageActionChain) {

        ScreenData<?> pageAction = pageActionChain.peek();
        onGo(container, pageAction);
    }
}
