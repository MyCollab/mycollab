package com.mycollab.module.project.view;

import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.UserNotBelongProjectException;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.module.project.view.user.ProjectDashboardPresenter;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.web.ui.AbstractPresenter;

import static com.mycollab.core.utils.ExceptionUtils.getExceptionType;

/**
 * @author MyCollab Ltd
 * @since 5.2.9
 */
public abstract class ProjectGenericPresenter<V extends PageView> extends AbstractPresenter<V> {
    public ProjectGenericPresenter(Class<V> viewClass) {
        super(viewClass);
    }

    @Override
    protected void onErrorStopChain(Throwable throwable) {
        super.onErrorStopChain(throwable);

        if (this instanceof ProjectViewPresenter) {
            if (getExceptionType(throwable, UserNotBelongProjectException.class) != null) {
                EventBusFactory.getInstance().post(new ProjectEvent.GotoUserDashboard(this, null));
            } else {
                EventBusFactory.getInstance().post(new ProjectEvent.GotoDashboard(this, null));
            }
        } else {
            EventBusFactory.getInstance().post(new ProjectEvent.GotoDashboard(this, null));
        }
    }

    @Override
    protected void onDefaultStopChain() {
        ProjectDashboardPresenter presenter = PresenterResolver.getPresenter(ProjectDashboardPresenter.class);
        presenter.go(view, null);
    }
}
