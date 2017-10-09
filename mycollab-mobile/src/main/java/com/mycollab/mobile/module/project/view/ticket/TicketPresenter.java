package com.mycollab.mobile.module.project.view.ticket;

import com.mycollab.core.MyCollabException;
import com.mycollab.mobile.module.project.view.bug.BugReadPresenter;
import com.mycollab.mobile.module.project.view.bug.IBugAddPresenter;
import com.mycollab.mobile.module.project.view.parameters.BugScreenData;
import com.mycollab.mobile.module.project.view.parameters.RiskScreenData;
import com.mycollab.mobile.module.project.view.parameters.TaskScreenData;
import com.mycollab.mobile.module.project.view.parameters.TicketScreenData;
import com.mycollab.mobile.module.project.view.risk.IRiskPresenter;
import com.mycollab.mobile.module.project.view.task.ITaskAddPresenter;
import com.mycollab.mobile.module.project.view.task.TaskReadPresenter;
import com.mycollab.mobile.mvp.AbstractPresenter;
import com.mycollab.mobile.mvp.view.PresenterOptionUtil;
import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.5.0
 */
public class TicketPresenter extends AbstractPresenter<TicketContainer> {
    private static final long serialVersionUID = 7999611450505328038L;

    public TicketPresenter() {
        super(TicketContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        IPresenter<?> presenter;

        if (data == null || data instanceof TicketScreenData.GotoDashboard) {
            presenter = PresenterResolver.getPresenter(TicketListPresenter.class);
        } else if (data instanceof TaskScreenData.Read) {
            presenter = PresenterResolver.getPresenter(TaskReadPresenter.class);
        } else if (data instanceof TaskScreenData.Add || data instanceof TaskScreenData.Edit) {
            presenter = PresenterOptionUtil.getPresenter(ITaskAddPresenter.class);
        } else if (data instanceof BugScreenData.Read) {
            presenter = PresenterResolver.getPresenter(BugReadPresenter.class);
        } else if (data instanceof BugScreenData.Add || data instanceof BugScreenData.Edit) {
            presenter = PresenterOptionUtil.getPresenter(IBugAddPresenter.class);
        } else if (data instanceof RiskScreenData.Read || data instanceof RiskScreenData.Add || data instanceof
                RiskScreenData.Edit) {
            presenter = PresenterResolver.getPresenter(IRiskPresenter.class);
        } else {
            throw new MyCollabException("Do not support param: " + data);
        }

        presenter.go(container, data);
    }
}
