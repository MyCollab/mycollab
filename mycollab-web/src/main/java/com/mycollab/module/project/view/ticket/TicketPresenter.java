package com.mycollab.module.project.view.ticket;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.ClassUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.view.ProjectView;
import com.mycollab.module.project.view.parameters.TaskScreenData;
import com.mycollab.module.project.view.parameters.TicketScreenData;
import com.mycollab.module.project.view.task.TaskAddPresenter;
import com.mycollab.module.project.view.task.TaskReadPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TicketPresenter extends AbstractPresenter<TicketContainer> {
    private static final long serialVersionUID = 1L;

    public TicketPresenter() {
        super(TicketContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectView projectViewContainer = (ProjectView) container;
        projectViewContainer.gotoSubView(ProjectTypeConstants.TICKET);

        AbstractPresenter<?> presenter;

        if (data instanceof TaskScreenData.Read) {
            presenter = PresenterResolver.getPresenter(TaskReadPresenter.class);
        } else if (ClassUtils.instanceOf(data, TaskScreenData.Edit.class, TaskScreenData.Add.class)) {
            presenter = PresenterResolver.getPresenter(TaskAddPresenter.class);
        } else if (data instanceof TaskScreenData.GotoKanbanView) {
            presenter = PresenterResolver.getPresenter(TicketKanbanBoardPresenter.class);
        } else if (data == null || data instanceof TicketScreenData.GotoDashboard) {
            presenter = PresenterResolver.getPresenter(TicketDashboardPresenter.class);
        } else {
            throw new MyCollabException("No support data: " + data);
        }

        presenter.go(view, data);
    }
}
